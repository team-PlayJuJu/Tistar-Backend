package com.juju.tistar.controller;

import com.juju.tistar.entity.User;
import com.juju.tistar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/dd")
    public ResponseEntity<User> dd() {
        User userId = userService.getCurrentUser();
        return ResponseEntity.ok(userId);
    }
}
