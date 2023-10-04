package com.cmj.myproject.service;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import com.cmj.myproject.domain.Post;
import com.cmj.myproject.dto.CommentDto;
import com.cmj.myproject.dto.PostDto;
import com.cmj.myproject.repository.CommentRepository;
import com.cmj.myproject.repository.PostRepository;
import com.cmj.myproject.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.cmj.myproject.util.RedisUtil.calculateTimeUntilMidnight;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final RedisUtil redisUtil;


    @Transactional
    public PostDto findPostById(Long id, String memberId) {
        // Redis에서 해당 게시물의 조회 여부 확인
        String viewCountKey = String.valueOf(id);
        String viewCount = redisUtil.getData(viewCountKey);

        if (viewCount == null) {
            // Redis에 해당 게시물의 조회 여부가 없는 경우
            // Redis에 조회 여부를 표시하고 조회수를 증가시킴
            redisUtil.setDateExpire(viewCountKey, memberId, calculateTimeUntilMidnight());
            postRepository.updateViewCnt(id);
        } else {
            // Redis에 해당 게시물의 조회 여부가 있는 경우
            // 중복 조회를 막기 위한 처리 (동일한 사용자가 아니면 조회수 증가)
            if (!viewCount.equals(memberId)) {
                postRepository.updateViewCnt(id);
                redisUtil.setDateExpire(viewCountKey, memberId, calculateTimeUntilMidnight());
            }
        }

        // 게시글 조회 및 반환
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return post.toDto();
    }


    public PostDto save(PostDto dto) {
        try {

            checkIfUserIsAuthor(dto.toEntity(), "글을 작성");
            Post savePost = postRepository.save(dto.toEntity());
            return savePost.toDto();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 저장 중에 문제가 발생했습니다.");
        }
    }


    public Page<PostDto> findPostById(Board board, Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Post> postList = postRepository.findByBoardId(board.getId(), pageable);

        //리퀘스트 페이지넘버가 보드리스트의 페이지넘버보다 크게 들어오면 에러가 발생한다.
        if (pageable.getPageNumber() > postList.getTotalPages()) {
            throw new IllegalArgumentException("해당 페이지가 존재하지 않습니다.");
        }

        return postList.map(Post::toDto);
    }

    public PostDto update(Long id, PostDto dto) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        try {

            checkIfUserIsAuthor(post, "수정");
            postRepository.save(post.update(dto));

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 수정 중에 문제가 발생했습니다.");
        }

        return post.toDto();

    }

    public void delete(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        try {

            checkIfUserIsAuthor(post, "삭제");
            postRepository.delete(post);

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 삭제 중에 문제가 발생했습니다.");
        }
    }

    private void checkIfUserIsAuthor(Post post, String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if ("anonymousUser".equals(email) && !email.equals(post.getWriterId())) {
            throw new IllegalArgumentException("작성자만 + " + category + "할 수 있습니다.");
        }
    }

    private void checkIfUserIsAuthor(Comment comment, String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if ("anonymousUser".equals(email) && !email.equals(comment.getWriterId())) {
            throw new IllegalArgumentException("작성자만 + " + category + "할 수 있습니다.");
        }
    }


    public List<CommentDto> findCommentList(Long id) {

        List<Comment> commentList = commentRepository.findByPostId(id);

        return commentList.stream().map(Comment::toDto).collect(Collectors.toList());
    }

    public void saveComment(Long id, CommentDto dto) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        try {

            checkIfUserIsAuthor(post, "댓글 작성");
            Comment comment = dto.toEntity();
            comment.setPost(post);

            commentRepository.save(comment);

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("댓글 저장 중에 문제가 발생했습니다.");
        }

    }

    public CommentDto findCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));
        return comment.toDto();
    }

    public CommentDto updateComment(Long commentId, CommentDto dto) {


        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));
        try {

            checkIfUserIsAuthor(comment, "수정");
            commentRepository.save(comment.update(dto));

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 수정 중에 문제가 발생했습니다.");
        }

        return comment.toDto();

    }

    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        try {
            checkIfUserIsAuthor(comment, "삭제");
            commentRepository.deleteById(commentId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("댓글 삭제 중에 문제가 발생했습니다.");
        }

    }

    //email로 최근 작성한 게시글 15개 가져오기
    public List<PostDto> findRecentPostByEmail(String email) {
        List<Post> postList = postRepository.findTop15ByWriterIdOrderByCreatedAtDesc(email);
        return postList.stream().map(Post::toDto).collect(Collectors.toList());
    }

    //email로 최근 작성한 댓글 15개 가져오기
    public List<CommentDto> findRecentCommentByEmail(String email) {
        List<Comment> commentList = commentRepository.findTop15ByWriterIdOrderByCreatedAtDesc(email);
        return commentList.stream().map(Comment::toDto).collect(Collectors.toList());
    }
}
