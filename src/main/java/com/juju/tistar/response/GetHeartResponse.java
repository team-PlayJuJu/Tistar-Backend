package com.juju.tistar.response;

public record GetHeartResponse (
        boolean isHeart,
        int HeartCount
) {
}