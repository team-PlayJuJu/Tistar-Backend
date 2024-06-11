package com.juju.tistar.mapper;

import com.juju.tistar.entity.Image;
import com.juju.tistar.response.UploadPostResponse;

public class PostMapper {
    public static Image uploadImageRequestToEntity(final String fileName, final String storeImagePath) {

        return new Image(
                fileName,
                storeImagePath
        );
    }

    public static UploadPostResponse entityToUploadImageResponse(final Image boardImage) {

        return new UploadPostResponse(
                boardImage.getImagePath(),
                boardImage.getId()
        );
    }


}
