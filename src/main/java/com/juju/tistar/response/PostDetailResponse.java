package com.juju.tistar.response;

import java.time.LocalDateTime;

public record PostDetailResponse(
        Long id,
        String content,
        Writer writer,
        LocalDateTime createdAt,
        long heartCount,
        boolean isHeart
) {
    public record Writer(
            String name
    ) {
    }
}
