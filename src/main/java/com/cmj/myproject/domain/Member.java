package com.cmj.myproject.domain;

import com.cmj.myproject.config.security.CustomUserDetails;
import com.cmj.myproject.dto.MemberResponseDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

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

    @Column(name="password", nullable = false)
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