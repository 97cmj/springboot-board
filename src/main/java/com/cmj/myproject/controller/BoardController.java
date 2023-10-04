package com.cmj.myproject.controller;

import com.cmj.myproject.config.security.CustomUserDetails;
import com.cmj.myproject.domain.Board;
import com.cmj.myproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;




    @RequestMapping("/list")
    public List<Board> list(@AuthenticationPrincipal CustomUserDetails userDetails) {


        //어드민인지 체크
        List<Board> list = null;
        if (userDetails.getAuthorities().toString().equals("[ADMIN]")) {
            list = boardService.selectAll();
        }

        return list;

    }

    @RequestMapping("/insert")
    public Map<String, String> insert(Board board, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, String> map = new HashMap<>();
        if (userDetails.getAuthorities().toString().equals("[ADMIN]")) {
            board.setName("자유게시판");
            board.setDescription("자유롭게 글을 쓸 수 있는 게시판입니다.");
            board.setUrl("free");
            boardService.insert(board);
            map.put("result", "success");
        } else {
            map.put("result", "fail");
        }

        return map;
    }


}
