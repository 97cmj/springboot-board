package com.cmj.myproject.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    // @Lob 어노테이션을 사용하여 CLOB, BLOB 타입을 매핑할 수 있다.
    @Lob
    private String content;

    private LocalDateTime createdDate;

    //한명의 유저는 여러개의 게시글을 작성할 수 있다.
    //Board 스키마에서는 user_id 라는 컬럼이 생성된다.(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}