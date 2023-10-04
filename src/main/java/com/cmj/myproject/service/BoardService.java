package com.cmj.myproject.service;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board findBoardByUrl(String url) {
        try {
            return boardRepository.findBoardByUrl(url);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("게시판이 존재하지 않습니다.");
        }
    }

    public void insert(Board board) {
        try {
            boardRepository.save(board);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("게시판이 존재하지 않습니다.");
        }
    }


    public List<Board> selectAll() {
        return boardRepository.findAll();
    }
}
