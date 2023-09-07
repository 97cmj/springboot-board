package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.CustomUserDetails;
import com.cmj.myproject.dto.BoardRequestDto;
import com.cmj.myproject.dto.BoardResponseDto;
import com.cmj.myproject.dto.MemberResponseDto;
import com.cmj.myproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.nio.file.attribute.UserPrincipal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    private final HttpSession session;

    @GetMapping("")
    public ModelAndView board(ModelAndView mv, @PageableDefault(size = 10, page = 1) Pageable pageable) {

        try {
            Page<BoardResponseDto> b = boardService.findAllBoard(pageable);
            mv.addObject("boardList", b);
            mv.setViewName("board/board_list");

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @GetMapping("/write")
    public ModelAndView write(ModelAndView mv, @AuthenticationPrincipal UserDetails userDetails) {

        log.info("userDetails: {}", userDetails);

        if (userDetails == null) {
            setErrorModelAndView(mv, new IllegalArgumentException("로그인이 필요합니다."));
            return mv;
        }

        try{
            mv.setViewName("board/board_write");
            mv.addObject("m", userDetails);

        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }
        return mv;
    }

    @PostMapping("/write")
    public ModelAndView write(BoardRequestDto dto, ModelAndView mv) {

        try {
            boardService.save(dto);
            mv.setViewName("redirect:/board/");
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }


    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id,
                               @RequestParam(defaultValue = "1") int page, ModelAndView mv) {

        try {
            BoardResponseDto dto = boardService.findBoardById(id, session.getId());
            mv.addObject("b", dto);
            mv.addObject("page", page);
            mv.setViewName("board/board_detail");
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @GetMapping("{id}/update")
    public ModelAndView update(@PathVariable("id") Long id, ModelAndView mv) {
        try {
            BoardResponseDto dto = boardService.findBoardById(id, session.getId());
            mv.addObject("b", dto);
            mv.setViewName("board/board_update");
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @PostMapping("{id}/update")
    public ModelAndView update(@PathVariable("id") Long id, BoardRequestDto dto, ModelAndView mv) {
        try {
            BoardResponseDto updatedBoard = boardService.update(id, dto);
            mv.setViewName("redirect:/board/" + updatedBoard.getId());
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }

        return mv;
    }

    @DeleteMapping("{id}/delete")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            boardService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    private void setErrorModelAndView(ModelAndView mv, Exception e) {
        mv.addObject("error", e.getMessage());
        mv.addObject("url", "/board");
        mv.setViewName("error/error");
    }


}
