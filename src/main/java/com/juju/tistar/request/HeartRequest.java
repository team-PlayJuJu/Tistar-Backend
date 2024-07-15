package com.juju.tistar.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HeartRequest(
        @NotBlank Long postId
){
}
