package com.juju.tistar.controller;

import com.juju.tistar.entity.User;
import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        return ResponseEntity.ok(userService.signupUser(user));
    }

//    @PostMapping("/signin")
//    public ResponseEntity<LoginRequest> signin(@Valid @RequestBody LoginRequest loginRequest) {
//
//    }
}
