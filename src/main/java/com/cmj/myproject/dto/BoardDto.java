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
public class BoardDto {

    private Long id;
    private String name;
    private String description;


}
