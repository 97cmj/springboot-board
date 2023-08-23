package com.cmj.myproject.controller;

import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signUpForm(){
        return "member/signup";
    }

    @PostMapping("/signup")
    public ModelAndView signUp(@ModelAttribute @Valid MemberRequestDto dto, BindingResult result) throws Exception {
        ModelAndView mv = new ModelAndView();

        if (result.hasErrors()) {
            mv.setViewName("member/signup");
            mv.addObject("errors", result.getAllErrors());
        } else {
            try {
                memberService.signUp(dto);
                mv.setViewName("redirect:/signup");
            } catch (IllegalArgumentException e) {
                mv.setViewName("member/signup");
                mv.addObject("error", e.getMessage());
            }
        }

        mv.addObject("dto", dto);
        return mv;
    }


}
