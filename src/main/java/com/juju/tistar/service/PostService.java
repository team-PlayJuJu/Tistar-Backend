package com.juju.tistar.service;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.juju.tistar.entity.Image;
import com.juju.tistar.repository.ImageRepository;
import com.juju.tistar.response.UploadPostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.juju.tistar.request.UploadPostRequest;
import java.io.IOException;
import java.util.UUID;

import com.juju.tistar.mapper.PostMapper;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public UploadPostResponse saveImage(final UploadPostRequest request) {
        final String originName = request.image().getOriginalFilename();
        final String ext = originName.substring(originName.lastIndexOf("."));
        final String changedImageName = changeImageName(ext);

        final String storeImagePath = uploadImage(request.image(), ext, changedImageName);

        final Image Image = PostMapper.uploadImageRequestToEntity(changedImageName, storeImagePath);
        imageRepository.save(Image);

        return PostMapper.entityToUploadImageResponse(Image);
    }

    private String uploadImage(final MultipartFile image,
                               final String ext,
                               final String changedImageName) {

        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));
        try {
            amazonS3.putObject(new PutObjectRequest(
                    bucket, changedImageName, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (final IOException e) {
            throw new RuntimeException("찾을 수 없음");
        }

        return amazonS3.getUrl(bucket, changedImageName).toString();
    }
    private String changeImageName(final String ext) {

        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
