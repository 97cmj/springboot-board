package com.cmj.myproject.config.security;

import com.cmj.myproject.domain.Member;
import com.cmj.myproject.dto.MemberResponseDto;
import com.cmj.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(email == null || email.equals("")){
            throw new UsernameNotFoundException("아이디를 입력해주세요.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        return Member.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }



}