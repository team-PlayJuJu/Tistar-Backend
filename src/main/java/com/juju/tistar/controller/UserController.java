package com.juju.tistar.controller;

import com.juju.tistar.entity.User;
import com.juju.tistar.request.SignupRequest;
import com.juju.tistar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupRequest> signup(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.ok(userService.signupUser(request));
    }

}
