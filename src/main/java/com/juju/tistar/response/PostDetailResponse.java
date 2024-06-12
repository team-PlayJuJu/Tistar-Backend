package com.juju.tistar.response;

import java.time.LocalDateTime;

public record PostDetailResponse(
        Long id,
        Writer writer,
        LocalDateTime createdAt,
        long heartCount,
        boolean isHeart,
        String tag
) {
    public record Writer(
            String name
    ) {
    }
}
