package com.juju.tistar.request;

import com.juju.tistar.entity.Post;
import jakarta.validation.constraints.NotNull;

public record CancelHeartRequest(
        @NotNull Long postId
        ) {

}
