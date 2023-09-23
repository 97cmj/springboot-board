package com.cmj.myproject.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberResponseDto {

    private String email;

    private String name;

    //패스워드는 보안상의 이유로 노출되면 안된다.
//    private String password;

    private String role;

}
