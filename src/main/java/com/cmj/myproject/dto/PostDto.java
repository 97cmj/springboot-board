package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Board;
import com.cmj.myproject.domain.Comment;
import com.cmj.myproject.domain.Post;
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
public class PostDto {

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

    private Board board;
    private List<Comment> comments;

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .writerId(writerId)
                .viewCnt(viewCnt)
                .recommendCnt(recommendCnt)
                .board(board)
                .build();
    }
}
