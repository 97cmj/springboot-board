package com.cmj.myproject.repository;

import com.cmj.myproject.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

        //desc는 내림차순, asc는 오름차순
        List<Comment> findByBoardId(Long boardId);




}
