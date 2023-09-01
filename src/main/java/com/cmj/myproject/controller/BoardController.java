package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.MemberAdapter;
import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @RequestMapping("")
    public String board(Model model) {

        Board b = Board.builder()
                .title("제목")
                .content("내용")
                .writer("작성자")
                .viewCnt(0)
                .recommendCnt(0)
                .replyCnt(0)
                .build();

        model.addAttribute("board", b);

        return "board/board_list";
    }

    @RequestMapping("/write")
    public ModelAndView write(@AuthenticationPrincipal MemberAdapter memberAdapter, ModelAndView mv) {
        Member m = (memberAdapter != null) ? memberAdapter.getMember() : MemberAdapter.createAnonymousMember();

        mv.addObject("m", m);

        mv.setViewName("board/board_write");
        return mv;
    }

}
