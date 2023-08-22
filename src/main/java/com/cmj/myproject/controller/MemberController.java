package com.cmj.myproject.controller;

import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signUpForm(){
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid MemberRequestDto dto) throws Exception {
        memberService.signUp(dto);

        return "redirect:member/signup";
    }

}
