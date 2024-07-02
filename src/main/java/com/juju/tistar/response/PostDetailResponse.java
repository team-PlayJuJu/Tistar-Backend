package com.juju.tistar.response;
import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Long id,
        String content,
        Writer writer,
        LocalDateTime createdAt,
        long heartCount,
        boolean isHeart,
        List<Image> imageUrls,
        List<Review> reviews
) {
    public record Writer(
            String name
    ) {
    }

    public record Review(
            Long id,
            String content,
            Long writerId,
            String writerName

    ) {
    }

    public record Image(
            Long id,
            String fileName,
            String imagePath
    ) {
    }
}
