package com.cmj.myproject.controller;

import com.cmj.myproject.dto.UserForm;
import com.cmj.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signUpUser(@RequestBody @Valid  UserForm userForm) throws Exception {
        userService.signUpUser(userForm);
    }
}
