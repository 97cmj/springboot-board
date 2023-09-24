package com.cmj.myproject.repository;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    //조회수 증가
    // 조회수 증가
    @Modifying
    @Query("UPDATE Board b SET b.viewCnt = b.viewCnt + 1 WHERE b.id = :id")
    int updateViewCnt(@Param("id") Long id);

    List<Board> findTop15ByWriterIdOrderByCreatedAtDesc(String email);
}
