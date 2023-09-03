package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.MemberAdapter;
import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Member;
import com.cmj.myproject.dto.BoardRequestDto;
import com.cmj.myproject.dto.BoardResponseDto;
import com.cmj.myproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("")
    public String board(Model model) {

        List<BoardResponseDto> b = boardService.findAllBoard();

        model.addAttribute("b", b);

        return "board/board_list";
    }

    @GetMapping("/write")
    public ModelAndView write(@AuthenticationPrincipal MemberAdapter memberAdapter, ModelAndView mv) {
        Member m = (memberAdapter != null) ? memberAdapter.getMember() : MemberAdapter.createAnonymousMember();

        mv.addObject("m", m);

        mv.setViewName("board/board_write");
        return mv;
    }

    @PostMapping("/write")
    public String write(BoardRequestDto dto, HttpServletResponse response) {

        BoardResponseDto savedBoard = boardService.save(dto);
        return "redirect:/board/" + savedBoard.getId();
    }


    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id, ModelAndView mv) {

        try {
            BoardResponseDto dto = boardService.findBoardById(id);
            mv.addObject("b", dto);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }


        mv.setViewName("board/board_detail");

        return mv;
    }




}
