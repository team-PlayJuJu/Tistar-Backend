package com.juju.tistar.controller;

import com.juju.tistar.annotation.Anyone;
import com.juju.tistar.annotation.UserId;
import com.juju.tistar.annotation.ValidFile;
import com.juju.tistar.request.UploadPostRequest;
import com.juju.tistar.request.WritePostRequest;
import com.juju.tistar.response.PostDetailResponse;
import com.juju.tistar.response.PostResponse;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.response.WritePostResponse;
import com.juju.tistar.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/images")
    public ResponseEntity<Void> image(@RequestPart(name = "file") MultipartFile[] images) {
        postService.saveImages(images);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/write")
    public ResponseEntity<WritePostResponse> write(@Valid WritePostRequest request, @UserId Long userId) {
        WritePostResponse data = postService.writePost(request,userId);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> read (@PathVariable final Long postId, @Anyone final Long accessId) {
        PostDetailResponse data = postService.readPost(postId, accessId);
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<Slice<PostResponse>> boardList(
            @RequestParam(name = "sortBy", defaultValue = "default") final String sortBy,
            @PageableDefault(size = 6) final Pageable pageable) {

        final Slice<PostResponse> data = postService.getPostList(sortBy, pageable);
        return ResponseEntity.ok(data);
    }


}