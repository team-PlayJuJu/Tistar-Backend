package com.juju.tistar.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewRequest {
    @NotNull
    private final Long postId;
    @NotNull
    private final String content;
}