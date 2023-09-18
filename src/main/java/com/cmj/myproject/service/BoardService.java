package com.cmj.myproject.service;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import com.cmj.myproject.dto.BoardDto;
import com.cmj.myproject.dto.CommentDto;
import com.cmj.myproject.repository.BoardRepository;
import com.cmj.myproject.repository.CommentRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private final RedisUtil redisUtil;


    @Transactional
    public BoardDto findBoardById(Long id, String memberId) {
        // Redis에서 해당 게시물의 조회 여부 확인
        String viewCountKey = String.valueOf(id);
        String viewCount = redisUtil.getData(viewCountKey);

        if (viewCount == null) {
            // Redis에 해당 게시물의 조회 여부가 없는 경우
            // Redis에 조회 여부를 표시하고 조회수를 증가시킴
            redisUtil.setDateExpire(viewCountKey, memberId, calculateTimeUntilMidnight());
            boardRepository.updateViewCnt(id);
        } else {
            // Redis에 해당 게시물의 조회 여부가 있는 경우
            // 중복 조회를 막기 위한 처리 (동일한 사용자가 아니면 조회수 증가)
            if (!viewCount.equals(memberId)) {
                boardRepository.updateViewCnt(id);
                redisUtil.setDateExpire(viewCountKey, memberId, calculateTimeUntilMidnight());
            }
        }

        // 게시글 조회 및 반환
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return board.toDto();
    }


    public BoardDto save(BoardDto dto) {
        try {

            checkIfUserIsAuthor(dto.toEntity(), "글을 작성");
            Board savedBoard = boardRepository.save(dto.toEntity());
            return savedBoard.toDto();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 저장 중에 문제가 발생했습니다.");
        }
    }


    public Page<BoardDto> findAllBoard(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Board> boardList = boardRepository.findAll(pageable);

        //리퀘스트 페이지넘버가 보드리스트의 페이지넘버보다 크게 들어오면 에러가 발생한다.
        if (pageable.getPageNumber() > boardList.getTotalPages()) {
            throw new IllegalArgumentException("해당 페이지가 존재하지 않습니다.");
        }

        return boardList.map(Board::toDto);
    }

    public BoardDto update(Long id, BoardDto dto) {

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        try {

            checkIfUserIsAuthor(board, "수정");
            boardRepository.save(board.update(dto));

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 수정 중에 문제가 발생했습니다.");
        }

        return board.toDto();

    }

    public void delete(Long id) {

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        try {

            checkIfUserIsAuthor(board, "삭제");
            boardRepository.delete(board);

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 삭제 중에 문제가 발생했습니다.");
        }
    }

    private void checkIfUserIsAuthor(Board board, String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if ("anonymousUser".equals(email) && !email.equals(board.getWriterId())) {
            throw new IllegalArgumentException("작성자만 + " + category + "할 수 있습니다.");
        }
    }




    public List<CommentDto> findCommentList(Long id) {

        List<Comment> commentList = commentRepository.findByBoardId(id);

         return commentList.stream().map(Comment::toDto).collect(Collectors.toList());
    }

    public void saveComment(Long id, CommentDto dto) {

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        try {

            checkIfUserIsAuthor(board, "댓글 작성");
            Comment comment = dto.toEntity();
            comment.setBoard(board);

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

//            checkIfUserIsAuthor(board, "수정");
            commentRepository.save(comment.update(dto));

        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 수정 중에 문제가 발생했습니다.");
        }

        return comment.toDto();

    }
}
