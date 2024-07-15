package com.juju.tistar.response;

import java.time.LocalDateTime;

public record PostResponse(
        Long postId,
        String imageUrl,
        LocalDateTime createdAt,
        Long heartCount
) {
}
