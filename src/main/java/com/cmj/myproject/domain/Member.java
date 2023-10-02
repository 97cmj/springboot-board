package com.cmj.myproject.domain;

import com.cmj.myproject.dto.MemberResponseDto;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Builder
@ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column
    private Role role;


    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .email(email)
                .name(name)
                .role(role.getKey())
                .build();
    }
}