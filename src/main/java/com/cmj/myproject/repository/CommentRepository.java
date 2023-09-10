package com.cmj.myproject.repository;

import com.cmj.myproject.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
        Page<Comment> findCommentsByBoardId(Long boardId, Pageable pageable);

}