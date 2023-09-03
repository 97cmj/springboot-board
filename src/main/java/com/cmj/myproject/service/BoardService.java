package com.cmj.myproject.service;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.dto.BoardRequestDto;
import com.cmj.myproject.dto.BoardResponseDto;
import com.cmj.myproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDto findBoardById(Long id) {

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return board.toDto();
    }

    public BoardResponseDto save(BoardRequestDto dto) {
        try {
            Board savedBoard = boardRepository.save(dto.toEntity());
            return savedBoard.toDto();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("게시글 저장 중에 문제가 발생했습니다.");
        }
    }


    public Page<BoardResponseDto> findAllBoard(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Board> boardList = boardRepository.findAll(pageable);

        //리퀘스트 페이지넘버가 보드리스트의 페이지넘버보다 크게 들어오면 에러가 발생한다.
        if(pageable.getPageNumber() > boardList.getTotalPages()) {
            throw new IllegalArgumentException("해당 페이지가 존재하지 않습니다.");
        }

        return boardList.map(Board::toDto);
    }
}
