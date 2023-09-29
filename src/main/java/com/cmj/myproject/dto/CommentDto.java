package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String writer;

    private String writerId;

    private String content;

    private int recommendCnt;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String createdBy;
    private String modifiedBy;

    private Board board;

    public Comment toEntity() {
        return Comment.builder()
                .writer(writer)
                .writerId(writerId)
                .content(content)
                .recommendCnt(recommendCnt)
                .build();
    }

}
