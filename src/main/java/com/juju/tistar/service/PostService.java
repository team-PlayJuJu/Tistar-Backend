package com.juju.tistar.service;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.juju.tistar.entity.Image;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.Tag;
import com.juju.tistar.entity.User;
import com.juju.tistar.repository.ImageRepository;
import com.juju.tistar.repository.PostRepository;
import com.juju.tistar.repository.TagRepository;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.WritePostRequest;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.response.WritePostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.juju.tistar.request.UploadPostRequest;
import java.io.IOException;
import java.util.List;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
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

    @Transactional
    public WritePostResponse writePost(final WritePostRequest request, final Long memberId) {

        final User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유저가 없음"));

        final Post post = new Post();
        post.setUser(user);
        saveTag(post, request.tag());
        postRepository.save(post);

        mappingPostAndImage(request, post);

        return PostMapper.entityToWritePostResponse(post);
    }

    private void saveTag(final Post post,
                         final String tagName) {

        if (StringUtils.hasText(tagName)) {
            final Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            post.setTag(tag);
        }
    }

    private void mappingPostAndImage(final WritePostRequest request, final Post post) {

        final List<Image> images = imageRepository.findAllImagesById(request.imageIds());
        images.forEach(image -> image.setPost(post));
    }
}
