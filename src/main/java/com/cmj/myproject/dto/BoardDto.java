package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private String writerId;
    private int viewCnt;
    private int recommendCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;

    private List<Comment> comments;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .writerId(writerId)
                .viewCnt(viewCnt)
                .recommendCnt(recommendCnt)
                .build();
    }



}
