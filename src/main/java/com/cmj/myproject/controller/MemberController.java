package com.cmj.myproject.controller;

import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.dto.MemberResponseDto;
import com.cmj.myproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public ModelAndView signUpForm(){
        return new ModelAndView("member/signup");
    }

    @PostMapping("/signup")
    public ModelAndView signUp(@ModelAttribute @Valid MemberRequestDto dto, BindingResult result, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView();

        if (result.hasErrors()) {
            mv.setViewName("member/signup");
            mv.addObject("error", result.getAllErrors());
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

    @GetMapping("/login")
    public ModelAndView loginForm(Model model, HttpServletRequest request){

        return new ModelAndView("member/login");
    }

    @GetMapping("/mypage/{email}")
    public ModelAndView mypage(@PathVariable String email) {
        ModelAndView mv = new ModelAndView();
        log.info("username = {}", email);

        try {
            MemberResponseDto m = memberService.findByEmail(email);
            mv.setViewName("member/mypage");
            mv.addObject("m", m);
        } catch (IllegalArgumentException e) {
            setErrorModelAndView(mv, e);
        }
        return mv;
    }


    public void setErrorModelAndView(ModelAndView mv, Exception e) {
        mv.setViewName("error/error");
        mv.addObject("error", e.getMessage());
        mv.addObject("url", "/");
    }

}
