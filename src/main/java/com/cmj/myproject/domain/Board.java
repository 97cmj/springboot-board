package com.cmj.myproject.domain;

import com.cmj.myproject.dto.BoardResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
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

    @Column(name = "view_cnt", nullable = false)
    private int viewCnt;

    @Column(name = "recommend_cnt", nullable = false)
    private int recommendCnt;

    @Column(name = "reply_cnt", nullable = false)
    private int replyCnt;

    public BoardResponseDto toDto() {
        return BoardResponseDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .viewCnt(viewCnt)
                .recommendCnt(recommendCnt)
                .replyCnt(replyCnt)
                .build();
    }


}
