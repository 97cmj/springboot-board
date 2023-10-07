package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.CustomUserDetails;
import com.cmj.myproject.domain.Board;
import com.cmj.myproject.dto.CommentDto;
import com.cmj.myproject.dto.PostDto;
import com.cmj.myproject.service.BoardService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService postService;
    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping(value = "/{url}", produces = "text/plain;charset=UTF-8")
    public ModelAndView board(@PathVariable String url, ModelAndView mv, @PageableDefault(size = 10, page = 1) Pageable pageable) {

        try {

            if (pageable.getPageNumber() == 0) {
                throw new IllegalArgumentException("해당 페이지가 존재하지 않습니다.");
            }

            Board b = boardService.findBoardByUrl(url);
            Page<PostDto> p = postService.findPostById(b, pageable);

            mv.addObject("boardList", p);
            mv.addObject("board", b);

            mv.setViewName("board/board_list");

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, url, e);
        }

        return mv;
    }

    @GetMapping(value = "/{url}/write", produces = "text/plain;charset=UTF-8")
    public ModelAndView write(@PathVariable String url, ModelAndView mv, @AuthenticationPrincipal CustomUserDetails userDetails) {


        if (userDetails == null) {
            setErrorModelAndView(mv, url, new IllegalArgumentException("로그인이 필요합니다."));
            return mv;
        }

        try {
            mv.setViewName("board/board_write");
            mv.addObject("m", userDetails);

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, url, e);
        }
        return mv;
    }

    @PostMapping(value = "/{url}/write", produces = "text/plain;charset=UTF-8")
    public ModelAndView write(@PathVariable String url, PostDto dto, ModelAndView mv) {

        try {
            dto.setBoard(boardService.findBoardByUrl(url));
            postService.save(dto);
            mv.setViewName("redirect:/board/" + url);
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, url, e);
        }

        return mv;
    }

    @GetMapping(value = "/{url}/{id}", produces = "text/plain;charset=UTF-8")
    public ModelAndView detail(@PathVariable("url") String url,
                               @PathVariable("id") Long id, ModelAndView mv,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               HttpServletRequest request) {

        try {
            PostDto dto = postService.findPostById(id, session.getId());
            dto.setBoard(boardService.findBoardByUrl(url));

            String page = request.getParameter("page");

            mv.addObject("page", page);

            mv.addObject("b", dto);

            mv.addObject("m", userDetails);
            mv.setViewName("board/board_detail");

            List<CommentDto> commentList = postService.findCommentList(id);

            mv.addObject("commentList", commentList);

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, url, e);
        }

        return mv;
    }


    @GetMapping(value = "{url}/{id}/update", produces = "text/plain;charset=UTF-8")
    public ModelAndView update(@PathVariable("url") String url,
                               @PathVariable("id") Long id, ModelAndView mv,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
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
            setErrorModelAndView(mv, url, e);
        }

        return mv;
    }

    @PostMapping(value = "{url}/{id}/update", produces = "text/plain;charset=UTF-8")
    public ModelAndView update(@PathVariable("url") String url,
                               @PathVariable("id") Long id,
                               PostDto dto, ModelAndView mv) {
        try {
            PostDto updatePost = postService.update(id, dto);
            mv.setViewName("redirect:/board/" + url + "/" + updatePost.getId());
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, url, e);
        }

        return mv;
    }

    @DeleteMapping(value = "{url}/{id}/delete", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("url") String url,
                                 @PathVariable("id") Long id) {
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
    @PostMapping(value = "{url}/{id}/comment", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public ResponseEntity comment(@PathVariable("url") String url,
                                  @PathVariable("id") Long id,
                                  CommentDto dto) {

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

    @PostMapping(value = "{url}/{id}/comment/{commentId}/update", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public ResponseEntity updateComment(@PathVariable("url") String url,
                                        @PathVariable("id") Long id,
                                        @PathVariable("commentId") Long commentId,
                                        CommentDto dto) {

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

    @DeleteMapping(value = "/{url}/{id}/comment/{commentId}/delete", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public ResponseEntity deleteComment(@PathVariable("url") String url,
                                        @PathVariable("id") Long id,
                                        @PathVariable("commentId") Long commentId) {

        try {
            postService.deleteComment(commentId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void setErrorModelAndView(ModelAndView mv, String url, Exception e) {
        mv.setViewName("error/error");
        mv.addObject("error", e.getMessage());
        mv.addObject("url", "/board/" + url);
    }


}
