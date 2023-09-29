package com.cmj.myproject.service;

import com.cmj.myproject.domain.Member;
import com.cmj.myproject.domain.Role;
import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.dto.MemberResponseDto;
import com.cmj.myproject.repository.BoardRepository;
import com.cmj.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signUp(MemberRequestDto dto) {
        validateDuplicateEmail(dto.getEmail());

        if (!StringUtils.equals(dto.getPassword(), dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .role(Role.valueOf(Role.MEMBER.getKey()))
                .build();

        memberRepository.save(member);

    }

    private void validateDuplicateEmail(String email) {
        if(memberRepository.existsByEmail(email)){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }


    public MemberResponseDto findByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return member.get().toDto();
    }
}
