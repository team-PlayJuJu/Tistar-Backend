package com.juju.tistar.mapper;

import com.juju.tistar.entity.Image;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.request.WritePostRequest;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.response.WritePostResponse;
import org.springframework.security.core.parameters.P;

public class PostMapper {

    public static Post writePostRequest(final WritePostRequest request, final User user) {

        return new Post(
                request.content(),
                user
        );
    }

    public static Image uploadImageRequest(final String fileName, final String storeImagePath) {

        return new Image(
                fileName,
                storeImagePath
        );
    }

    public static UploadPostResponse UploadImageResponse(final Image image) {

        return new UploadPostResponse(
                image.getImagePath(),
                image.getId()
        );
    }

    public static WritePostResponse WritePostResponse(final Post post) {

        return new WritePostResponse(
                post.getId(),
                post.getUser().getName()
        );
    }

}
