package com.juju.tistar.service;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.juju.tistar.entity.Image;
import com.juju.tistar.entity.Post;
import com.juju.tistar.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.juju.tistar.request.CreatePostRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import com.amazonaws.services.s3.AmazonS3;
import com.juju.tistar.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AmazonS3 amazonS3;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void createPost(CreatePostRequest request) {
        Post post = new Post();
        post.setTag(request.getTag());
        post.setHeart(0L);
        postRepository.save(post);

        List<Image> images = new ArrayList<>();
        for (MultipartFile image : request.getImages()) {
            String originName = image.getOriginalFilename();
            String ext = originName.substring(originName.lastIndexOf("."));
            String changedImageName = changeImageName(ext);
            String storeImagePath = uploadImage(image, ext, changedImageName);

            Image img = new Image();
            img.setFileName(changedImageName);
            img.setImagePath(storeImagePath);
            img.setPost(post);
            imageRepository.save(img);

            images.add(img);
        }
        post.setImages(new HashSet<>(images));
    }

    private String uploadImage(final MultipartFile image, final String ext, final String changedImageName) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));
        try {
            amazonS3.putObject(new PutObjectRequest(
                    bucket, changedImageName, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (final IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
        return amazonS3.getUrl(bucket, changedImageName).toString();
    }

    private String changeImageName(final String ext) {
        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }
}
