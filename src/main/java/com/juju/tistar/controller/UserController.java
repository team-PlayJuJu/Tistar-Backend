package com.juju.tistar.controller;

import com.juju.tistar.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("회원가입 성공");
    }

    @GetMapping("/signin")
    public String signin() {
        return "로그인";
    }
}
