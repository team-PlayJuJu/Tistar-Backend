package com.juju.tistar.controller;

import com.juju.tistar.request.UploadPostRequest;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<UploadPostResponse> post(@ModelAttribute @Valid final UploadPostRequest request) {
        UploadPostResponse data = postService.saveImage(request);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/home")
    public String home() {
        return "images";
    }

    @GetMapping("/{userId}/myPage")
    public String myPage() {
        return "myImage";
    }
}