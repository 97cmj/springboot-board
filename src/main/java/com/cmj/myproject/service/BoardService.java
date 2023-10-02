package com.cmj.myproject.service;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board findBoardByName(String name) {
        try {
            return boardRepository.findByName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("게시판이 존재하지 않습니다.");
        }
    }
}
