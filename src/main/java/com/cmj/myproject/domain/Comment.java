package com.cmj.myproject.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String writerId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int recommendCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Board board;
}