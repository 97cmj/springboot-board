package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardRequestDto {

    private String title;
    private String content;
    private String writer;
    private String writerId;
    private String password;

    public Board toEntity() {

        return Board.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .writerId(writerId)
                .password(password)
                .build();
    }



}
