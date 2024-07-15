package com.juju.tistar.controller;

import com.juju.tistar.request.HeartRequest;
import com.juju.tistar.response.CancelHeartResponse;
import com.juju.tistar.response.GetHeartResponse;
import com.juju.tistar.response.HeartResponse;
import com.juju.tistar.service.HeartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hearts")
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;
    @PostMapping
    public ResponseEntity<HeartResponse> registerLike(@RequestParam("postId") final Long postId) {
        HeartResponse data = heartService.registerHeart(postId);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping
    public ResponseEntity<CancelHeartResponse> cancelLike(@RequestParam("postId") final Long postId) {
        CancelHeartResponse data = heartService.cancelHeart(postId);
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<GetHeartResponse> getLikeStatus(@RequestParam("postId") final Long postId) {
        GetHeartResponse data = heartService.getHeart(postId);
        return ResponseEntity.ok(data);
    }
}
