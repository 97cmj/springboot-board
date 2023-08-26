package com.cmj.myproject.service;

import com.cmj.myproject.domain.Member;
import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.exception.DuplicateEmailException;
import com.cmj.myproject.repository.MemberRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public void signUp(MemberRequestDto dto) throws Exception {
        validateDuplicateEmail(dto.getEmail());

        if (!StringUtils.equals(dto.getPassword(), dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateDuplicateEmail(String email) throws Exception {
        if(memberRepository.existsByEmail(email)){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }


    public void login(MemberRequestDto dto) {

        Member member = memberRepository.findByEmail(dto.getEmail());

        if(member == null){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        log.info(String.valueOf(member));



    }
}
