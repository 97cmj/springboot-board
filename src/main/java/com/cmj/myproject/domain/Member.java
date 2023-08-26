package com.cmj.myproject.domain;


import com.cmj.myproject.dto.MemberRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    public Member() {}

    public Member(MemberRequestDto dto) {
        this.email = dto.getEmail();
        this.name = dto.getName();
        this.password = dto.getPassword();
    }

    public static Member createMember(MemberRequestDto dto){
        return new Member(dto);
    }
}
