package com.cmj.myproject.config.security;

import com.cmj.myproject.domain.Member;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@ToString
public class MemberAdapter extends User {

    private Member member;

    public MemberAdapter(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getAuthorities().toString())));
        this.member = member;
    }

    public static Member createAnonymousMember() {
        return new Member(1L, "anonymous@gmail.com", "", "익명");
    }




}
