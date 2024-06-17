package com.juju.tistar.request;

import com.juju.tistar.annotation.ValidFile;
import org.springframework.web.multipart.MultipartFile;

public record UploadPostRequest (
        @ValidFile MultipartFile[] image
) {
}