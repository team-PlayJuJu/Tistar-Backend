package com.juju.tistar.controller;

import com.juju.tistar.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping
    public void createReview(
            @RequestParam Long postId,
            @RequestBody String content) {
        reviewService.createReview(postId, content);
    }
}
