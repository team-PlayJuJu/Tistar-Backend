package com.juju.tistar.controller;

import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.response.LoginResponse;
import com.juju.tistar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<LoginRequest> signup(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userService.signupUser(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signin(@Valid @RequestBody LoginRequest request){
        LoginResponse response = userService.signinUser(request);
        return ResponseEntity.ok().body(response);
    }
}
