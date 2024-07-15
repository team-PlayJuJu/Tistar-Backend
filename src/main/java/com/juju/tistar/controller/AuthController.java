package com.juju.tistar.controller;

import com.juju.tistar.request.LoginRequest;
import com.juju.tistar.request.SignupRequest;
import com.juju.tistar.response.LoginResponse;
import com.juju.tistar.response.RefreshTokenResponse;
import com.juju.tistar.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<LoginRequest> signup(@RequestBody @Valid SignupRequest request) {
        userService.signupUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signin(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.signinUser(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request){
        String accessToken = request.getHeader("access");
        String refreshToken = request.getHeader("refresh");

        return ResponseEntity.ok(userService.refreshToken(accessToken, refreshToken));
    }
}
