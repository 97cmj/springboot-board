package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Member;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Valid
@Getter
@Builder
public class MemberRequestDto {

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    public MemberRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }



}
