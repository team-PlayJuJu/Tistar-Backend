package com.juju.tistar.response;

import java.time.LocalDateTime;

public record MyHeartPostsResponse(
        Long postId,
        String imageUrl,
        LocalDateTime createdAt
) {
}
