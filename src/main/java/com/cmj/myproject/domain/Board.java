package com.cmj.myproject.domain;

import com.cmj.myproject.dto.BoardDto;
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
    @Column(name = "board_id", updatable = false)
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    public BoardDto toDto() {
        return BoardDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .writerId(writerId)
                .viewCnt(viewCnt)
                .recommendCnt(recommendCnt)
                .createdBy(getCreatedBy())
                .createdAt(getCreatedAt())
                .modifiedAt(getModifiedAt())
                .modifiedBy(getModifiedBy())
                .build();
    }

    public Board update(BoardDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        return this;
    }


}
