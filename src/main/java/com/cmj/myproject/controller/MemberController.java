package com.cmj.myproject.controller;

import com.cmj.myproject.dto.CommentDto;
import com.cmj.myproject.dto.MemberRequestDto;
import com.cmj.myproject.dto.MemberResponseDto;
import com.cmj.myproject.dto.PostDto;
import com.cmj.myproject.service.MemberService;
import com.cmj.myproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/signup")
    public ModelAndView signUpForm() {
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
    public ModelAndView loginForm(Model model, HttpServletRequest request) {

        return new ModelAndView("member/login");
    }

    @GetMapping("/mypage/{email}")
    public ModelAndView mypage(@PathVariable String email) {
        ModelAndView mv = new ModelAndView();

        try {
            MemberResponseDto m = memberService.findByEmail(email);
            List<PostDto> b = postService.findRecentPostByEmail(email);
            List<CommentDto> c = postService.findRecentCommentByEmail(email);

            mv.setViewName("member/mypage");
            mv.addObject("m", m);
            mv.addObject("b", b);
            mv.addObject("c", c);
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
