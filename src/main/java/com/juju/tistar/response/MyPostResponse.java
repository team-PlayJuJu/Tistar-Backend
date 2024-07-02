package com.juju.tistar.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record MyPostResponse(
        Long postId,
        String[] imageUrl
){
}
