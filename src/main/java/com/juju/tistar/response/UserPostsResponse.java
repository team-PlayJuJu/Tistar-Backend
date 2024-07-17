package com.juju.tistar.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserPostsResponse(
        Long postId,
        String imageUrl,
        LocalDateTime createdAt
){
}
