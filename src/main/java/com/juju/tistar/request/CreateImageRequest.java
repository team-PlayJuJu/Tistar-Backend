package com.juju.tistar.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateImageRequest {
    private final List<String> images;
    private final String content;
    private final String tag;
}
