package com.cmj.myproject.domain;


import com.cmj.myproject.dto.PostDto;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    @Column(name = "post_title", nullable = false)
    private String title;

    @Column(name = "post_content", nullable = false)
    private String content;

    @Column(name = "post_writer", nullable = false)
    private String writer;

    @Column(name = "post_writer_id", nullable = false)
    private String writerId;

    @Column(name = "post_view_cnt", nullable = false)
    private int viewCnt;

    @Column(name = "post_recommend_cnt", nullable = false)
    private int recommendCnt;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public PostDto toDto() {
        return PostDto.builder()
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

    public Post update(PostDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        return this;
    }
}
