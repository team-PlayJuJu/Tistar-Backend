package com.juju.tistar.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ImageDetailView {
    private final Long userId;
    private final List<String> images;
    private final String content;
    private final String tag;
    private final Long heart;
}
