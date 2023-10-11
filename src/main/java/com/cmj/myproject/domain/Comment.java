package com.cmj.myproject.domain;

import com.cmj.myproject.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(id)
                .writer(writer)
                .writerId(writerId)
                .content(content)
                .recommendCnt(recommendCnt)
                .post(post)
                .build();
    }

    public Comment update(CommentDto dto) {
        this.content = dto.getContent();
        return this;
    }

}
