package com.juju.tistar.controller;

import com.juju.tistar.annotation.Anyone;
import com.juju.tistar.annotation.UserId;
import com.juju.tistar.request.UploadPostRequest;
import com.juju.tistar.request.WritePostRequest;
import com.juju.tistar.response.PostDetailResponse;
import com.juju.tistar.response.PostResponse;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.response.WritePostResponse;
import com.juju.tistar.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/image")
    public ResponseEntity<UploadPostResponse> image(@ModelAttribute @Valid UploadPostRequest request) {
        UploadPostResponse data = postService.saveImage(request);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/write")
    public ResponseEntity<WritePostResponse> write(@Valid WritePostRequest request, @UserId Long userId) {
        WritePostResponse data = postService.writePost(request,userId);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> read (@PathVariable final Long boardId, @Anyone final Long accessId,) {
        PostDetailResponse data = postService.readPost(boardId, accessId);
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