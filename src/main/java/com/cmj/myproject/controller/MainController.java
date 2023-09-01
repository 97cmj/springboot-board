package com.cmj.myproject.controller;


import com.cmj.myproject.config.security.MemberAdapter;
import com.cmj.myproject.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@Slf4j
public class MainController {

    @RequestMapping("/")
    public String main(@AuthenticationPrincipal MemberAdapter memberAdapter, Model model) {
        Member m = (memberAdapter != null) ? memberAdapter.getMember() : MemberAdapter.createAnonymousMember();

        model.addAttribute("name", m.getName());
        return "main/main";
    }
}
