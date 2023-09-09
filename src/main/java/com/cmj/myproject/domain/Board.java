package com.cmj.myproject.domain;

import com.cmj.myproject.dto.BoardRequestDto;
import com.cmj.myproject.dto.BoardResponseDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;

    @Column(name = "writer_id", nullable = false)
    private String writerId;

    @Column(name = "view_cnt", nullable = false)
    private int viewCnt;

    @Column(name = "recommend_cnt", nullable = false)
    private int recommendCnt;

    @Column(name = "reply_cnt", nullable = false)
    private int replyCnt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;


    public BoardResponseDto toDto() {
        return BoardResponseDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .writerId(writerId)
                .viewCnt(viewCnt)
                .recommendCnt(recommendCnt)
                .replyCnt(replyCnt)
                .build();
    }

    public Board update(BoardRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        return this;
    }


}
