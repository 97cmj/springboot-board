package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Comment toEntity() {
        return Comment.builder()
                .writer(writer)
                .writerId(writerId)
                .content(content)
                .recommendCnt(recommendCnt)
                .build();
    }

}
