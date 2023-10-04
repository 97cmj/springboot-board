package com.cmj.myproject.repository;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import com.cmj.myproject.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 조회수 증가
    @Modifying
    @Query("UPDATE Post p SET p.viewCnt = p.viewCnt + 1 WHERE p.id = :id")
    int updateViewCnt(@Param("id") Long id);

    List<Post> findTop15ByWriterIdOrderByCreatedAtDesc(String email);


    Page<Post> findByBoardId(Long id, Pageable pageable);
}
