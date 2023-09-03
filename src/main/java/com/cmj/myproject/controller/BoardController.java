package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.MemberAdapter;
import com.cmj.myproject.domain.Member;
import com.cmj.myproject.dto.BoardRequestDto;
import com.cmj.myproject.dto.BoardResponseDto;
import com.cmj.myproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public ModelAndView board(ModelAndView mv, @PageableDefault(size = 10, page = 1) Pageable pageable) {

        try {
            Page<BoardResponseDto> b = boardService.findAllBoard(pageable);
            mv.addObject("boardList", b);
            mv.setViewName("board/board_list");

        } catch (IllegalArgumentException e) {
            mv.addObject("error", e.getMessage());
            mv.addObject("url", "/board");
            mv.setViewName(("error/error"));
        }

        return mv;
    }

    @GetMapping("/write")
    public ModelAndView write(@AuthenticationPrincipal MemberAdapter memberAdapter, ModelAndView mv) {
        Member m = (memberAdapter != null) ? memberAdapter.getMember() : MemberAdapter.createAnonymousMember();

        mv.addObject("m", m);

        mv.setViewName("board/board_write");
        return mv;
    }

    @PostMapping("/write")
    public ModelAndView write(BoardRequestDto dto, ModelAndView mv) {

        BoardResponseDto savedBoard = boardService.save(dto);
        mv.setViewName("redirect:/board/" + savedBoard.getId());

        return mv;
    }


    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id, @RequestParam(defaultValue = "1") int page, ModelAndView mv) {
        
        try {
            BoardResponseDto dto = boardService.findBoardById(id);
            mv.addObject("b", dto);
            mv.addObject("page", page);
            mv.setViewName("board/board_detail");
        } catch (IllegalArgumentException e) {
            mv.addObject("error", e.getMessage());
            mv.addObject("url", "/board");
            mv.setViewName(("error/error"));
        }

        return mv;
    }


}
