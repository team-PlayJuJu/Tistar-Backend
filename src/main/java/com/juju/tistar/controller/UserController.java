package com.juju.tistar.controller;

import com.juju.tistar.entity.User;
import com.juju.tistar.response.UserPageResponse;
import com.juju.tistar.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/userpage")
    public ResponseEntity<UserPageResponse> userPage(@RequestBody @NotBlank String name, Pageable pageable) {
        UserPageResponse data = userService.getUserPage(name.trim(), pageable);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/mypage")
    public ResponseEntity<UserPageResponse> myPagePost(Pageable pageable) {
        User user = userService.getCurrentUser();
        UserPageResponse data = userService.getUserPage(user.getName(), pageable);
        return ResponseEntity.ok(data);
    }
    @GetMapping("/myhearts")
    public ResponseEntity<UserPageResponse> myPageLike(Pageable pageable) {
        User user = userService.getCurrentUser();
        UserPageResponse data = userService.getUserHearts(user.getName(), pageable);
        return ResponseEntity.ok(data);
    }
}
