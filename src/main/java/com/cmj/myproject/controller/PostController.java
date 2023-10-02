package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.CustomUserDetails;
import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Category;
import com.cmj.myproject.dto.BoardDto;
import com.cmj.myproject.dto.CommentDto;
import com.cmj.myproject.dto.PostDto;
import com.cmj.myproject.service.BoardService;
import com.cmj.myproject.service.CategoryService;
import com.cmj.myproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/b")
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping("")
    public ModelAndView board(@PathVariable String category, ModelAndView mv, @PageableDefault(size = 10, page = 1) Pageable pageable) {
        try {

            Board b = boardService.findBoardByName(category);
            Page<PostDto> p = postService.findPostByCategory(b, pageable);

            mv.addObject("boardList", p);

            mv.setViewName("board/board_list");

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @GetMapping("/write")
    public ModelAndView write(ModelAndView mv, @AuthenticationPrincipal CustomUserDetails userDetails) {


        if (userDetails == null) {
            setErrorModelAndView(mv, new IllegalArgumentException("로그인이 필요합니다."));
            return mv;
        }

        try {
            mv.setViewName("board/board_write");
            mv.addObject("m", userDetails);

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }
        return mv;
    }

    @PostMapping("/write")
    public ModelAndView write(PostDto dto, ModelAndView mv) {

        try {
            postService.save(dto);
            mv.setViewName("redirect:/board/");
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }


    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id,
                               @RequestParam(defaultValue = "1", value = "page") int page, ModelAndView mv,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            PostDto dto = postService.findPostById(id, session.getId());
            mv.addObject("b", dto);

            mv.addObject("m", userDetails);
            mv.setViewName("board/board_detail");

            List<CommentDto> commentList = postService.findCommentList(id);

            mv.addObject("commentList", commentList);

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @GetMapping("{id}/update")
    public ModelAndView update(@PathVariable("id") Long id, ModelAndView mv, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            PostDto dto = postService.findPostById(id, session.getId());

            if (userDetails == null) {
                throw new IllegalArgumentException("로그인이 필요합니다.");
            }
            if (!dto.getWriterId().equals(userDetails.getUsername())) {
                throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
            } else {
                mv.addObject("b", dto);
                mv.setViewName("board/board_update");
            }

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @PostMapping("{id}/update")
    public ModelAndView update(@PathVariable("id") Long id, PostDto dto, ModelAndView mv) {
        try {
            PostDto updatePost = postService.update(id, dto);
            mv.setViewName("redirect:/board/" + updatePost.getId());
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @DeleteMapping("{id}/delete")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            postService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //댓글 작성
    @PostMapping("{id}/comment")
    @ResponseBody
    public ResponseEntity comment(@PathVariable("id") Long id, CommentDto dto) {

        try {
            postService.saveComment(id, dto);
            List<CommentDto> commentList = postService.findCommentList(id);


            return new ResponseEntity(commentList, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{id}/comment/{commentId}/update")
    @ResponseBody
    public ResponseEntity updateComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId, CommentDto dto) {

        try {
            postService.updateComment(commentId, dto);

            CommentDto comment = postService.findCommentById(commentId);

            return new ResponseEntity(comment, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}/comment/{commentId}/delete")
    @ResponseBody
    public ResponseEntity deleteComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {

        try {
            postService.deleteComment(commentId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void setErrorModelAndView(ModelAndView mv, Exception e) {
        mv.setViewName("error/error");
        mv.addObject("error", e.getMessage());
        mv.addObject("url", "/");
    }


}