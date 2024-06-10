package com.juju.tistar.response;

import com.juju.tistar.entity.Image;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<Image> images;
}
