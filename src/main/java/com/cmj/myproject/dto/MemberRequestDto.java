package com.cmj.myproject.dto;

import com.cmj.myproject.domain.Member;
import com.cmj.myproject.domain.Role;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Valid
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String passwordConfirm;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    private Role role;


}
