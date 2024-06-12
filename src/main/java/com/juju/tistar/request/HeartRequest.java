package com.juju.tistar.request;

import jakarta.validation.constraints.NotNull;

public record HeartRequest(
        @NotNull Long postId
){
}
