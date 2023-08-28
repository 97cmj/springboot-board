package com.cmj.myproject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MainController {

    @RequestMapping("/")
    public String main(Principal principal, Model model) {
        String name = principal.getName();
        model.addAttribute("name", name);
        return "main/main";
    }
}
