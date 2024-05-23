package com.juju.tistar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/signup")
    public String signUp() {
        return "회원가입";
    }

    @GetMapping("/signin")
    public String signin() {
        return "로그인";
    }
}
