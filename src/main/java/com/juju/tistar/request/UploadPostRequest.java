package com.juju.tistar.request;

import org.springframework.web.multipart.MultipartFile;

public record UploadPostRequest (
        MultipartFile image
) {
}