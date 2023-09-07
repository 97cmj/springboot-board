package com.cmj.myproject.dto;


import com.cmj.myproject.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private String writerId;
    private int viewCnt;
    private int recommendCnt;
    private int replyCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;

    public Board toEntity() {
        return Board.builder()
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


}
