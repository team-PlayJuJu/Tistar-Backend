package com.juju.tistar.request;
import jakarta.validation.constraints.NotBlank;

public record WritePostRequest (
        @NotBlank String content
)    {
}
