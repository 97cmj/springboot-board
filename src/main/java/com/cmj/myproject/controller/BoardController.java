package com.cmj.myproject.controller;

import com.cmj.myproject.domain.Board;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BoardController {

    @RequestMapping("/board")
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

        return "board/boardList";
    }

}
