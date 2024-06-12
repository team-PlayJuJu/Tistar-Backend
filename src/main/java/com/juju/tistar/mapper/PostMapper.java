package com.juju.tistar.mapper;

import com.juju.tistar.entity.Image;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.response.WritePostResponse;

public class PostMapper {
    public static Image uploadImageRequestToEntity(final String fileName, final String storeImagePath) {

        return new Image(
                fileName,
                storeImagePath
        );
    }

    public static UploadPostResponse entityToUploadImageResponse(final Image image) {

        return new UploadPostResponse(
                image.getImagePath(),
                image.getId()
        );
    }

    public static WritePostResponse entityToWritePostResponse(final Post post) {

        return new WritePostResponse(
                post.getId(),
                post.getUser().getName()
        );
    }

}
