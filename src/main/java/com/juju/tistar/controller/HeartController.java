package com.juju.tistar.controller;

import com.juju.tistar.annotation.Anyone;
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
    public ResponseEntity<HeartResponse> registerLike(@Anyone final Long accessId,
                                                      @RequestAttribute("isAuthenticated") final boolean isAuthenticated,
                                                      @RequestBody @Valid final HeartRequest request) {
        HeartResponse data;

        if (isAuthenticated) {
            data = heartService.registerHeart(accessId, request);
        } else {
            throw new RuntimeException("권한이 없습니다");
        }
        return ResponseEntity.ok(data);
    }

    @DeleteMapping
    public ResponseEntity<CancelHeartResponse> cancelLike(@Anyone final Long accessId,
                                                          @RequestAttribute("isAuthenticated") final boolean isAuthenticated,
                                                          @RequestParam("postId") @Valid final Long postId) {
        CancelHeartResponse data;

        if (isAuthenticated) {
            data = heartService.cancelHeart(accessId, postId);
        } else {
            throw new RuntimeException("권한이 없습니다");
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<GetHeartResponse> getLikeStatus(@Anyone final Long accessId,
                                                          @RequestAttribute("isAuthenticated") final boolean isAuthenticated,
                                                          @RequestParam("postId") final Long postId) {

        GetHeartResponse data;

        if (isAuthenticated) {
            data = heartService.getHeart(accessId, postId);
        } else {
            throw new RuntimeException("권한이 없습니다");
        }
        return ResponseEntity.ok(data);
    }
}
