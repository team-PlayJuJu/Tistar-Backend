package com.juju.tistar.request;

import com.juju.tistar.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CancelHeartRequest(
        @NotBlank Long postId
        ) {
}
