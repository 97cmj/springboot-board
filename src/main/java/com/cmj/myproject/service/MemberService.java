package com.cmj.myproject.service;

import com.cmj.myproject.domain.Member;
import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.exception.DuplicateEmailException;
import com.cmj.myproject.repository.MemberRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public void signUp(MemberRequestDto dto) throws Exception {
        validateDuplicateEmail(dto.getEmail());

        if(!dto.getPassword().equals(dto.getPasswordConfirm())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.save(Member.createMember(dto));
    }

    private void validateDuplicateEmail(String email) throws Exception {
        if(memberRepository.existsByEmail(email)){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }



}
