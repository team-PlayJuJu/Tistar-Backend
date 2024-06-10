package com.juju.tistar.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    private String tag;
    private List<MultipartFile> images;
}
