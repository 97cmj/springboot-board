package com.cmj.myproject.domain;

import com.cmj.myproject.dto.BoardDto;
import com.cmj.myproject.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false)
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
    @JoinColumn(name = "board_id")
    private Board board; // Represents the many-to-one relationship with Board

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(id)
                .writer(writer)
                .writerId(writerId)
                .content(content)
                .recommendCnt(recommendCnt)
                .board(board)
                .build();
    }

    public Comment update(CommentDto dto) {
        this.content = dto.getContent();
        return this;
    }

}
