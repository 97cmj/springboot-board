package com.cmj.myproject.repository;

import com.cmj.myproject.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //desc는 내림차순, asc는 오름차순
    List<Comment> findByPostId(Long postId);

    List<Comment> findTop15ByWriterIdOrderByCreatedAtDesc(String email);
}
