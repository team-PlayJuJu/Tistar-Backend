package com.juju.tistar.controller;

import com.juju.tistar.response.PostDetailResponse;
import com.juju.tistar.response.PostResponse;
import com.juju.tistar.response.WritePostResponse;
import com.juju.tistar.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/write")
    public ResponseEntity<WritePostResponse> createPost(
            @RequestPart("content") String content,
            @RequestPart("images") List<MultipartFile> files) {
        WritePostResponse response = postService.writePost(content, files);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> read (@PathVariable final Long postId) {
        PostDetailResponse data = postService.readPost(postId);
        return ResponseEntity.ok(data);
    }

    @GetMapping 
    public ResponseEntity<Slice<PostResponse>> boardList(
            @RequestParam(name = "sortBy", defaultValue = "default") final String sortBy,
            @PageableDefault(size = 6) final Pageable pageable) {

        final Slice<PostResponse> data = postService.getPostList(sortBy, pageable);
        return ResponseEntity.ok(data);
    }
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable final Long postId){
        postService.deletePost(postId);
    }
}