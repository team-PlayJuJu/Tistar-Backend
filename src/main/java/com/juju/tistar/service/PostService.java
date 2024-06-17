package com.juju.tistar.service;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.juju.tistar.entity.Image;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.entity.enums.SortType;
import com.juju.tistar.repository.ImageRepository;
import com.juju.tistar.repository.PostQueryRepository;
import com.juju.tistar.repository.PostRepository;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.WritePostRequest;
import com.juju.tistar.response.PostDetailResponse;
import com.juju.tistar.response.PostResponse;
import com.juju.tistar.response.UploadPostResponse;
import com.juju.tistar.response.WritePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.juju.tistar.request.UploadPostRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.juju.tistar.mapper.PostMapper;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostQueryRepository postQueryRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void saveImages(MultipartFile[] files) {

        for (MultipartFile file : files) {
            String originName = file.getOriginalFilename();
            if (originName == null || originName.isEmpty()) {
                throw new IllegalArgumentException("파일 이름 없음");
            }
            String ext = originName.substring(originName.lastIndexOf("."));
            String changedImageName = changeImageName(ext);
            String storeImagePath = uploadImage(file, ext, changedImageName);

            Image image = PostMapper.uploadImageRequest(changedImageName, storeImagePath);
            imageRepository.save(image);
            PostMapper.UploadImageResponse(image);
        }
    }

    private String uploadImage(final MultipartFile file, final String ext, final String changedImageName) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext.substring(1));
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(
                    bucket, changedImageName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (final IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
        return amazonS3.getUrl(bucket, changedImageName).toString();
    }

    private String changeImageName(final String ext) {
        final String uuid = UUID.randomUUID().toString();
        return uuid + ext;
    }

    @Transactional
    public WritePostResponse writePost(final WritePostRequest request, final Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 없음"));

        final Post post = PostMapper.writePostRequest(request, user);
        post.setUser(user);
        postRepository.save(post);

        mappingPostAndImage(request, post);

        return PostMapper.WritePostResponse(post);
    }



    private void mappingPostAndImage(final WritePostRequest request, final Post post) {

        final List<Image> images = imageRepository.findAllImagesById(request.imageIds());
        images.forEach(image -> image.setPost(post));
    }

    @Transactional
    public PostDetailResponse readPost(final Long postId, final Long accessId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없음"));

        return postQueryRepository.readPost(postId, accessId);
    }

    public Slice<PostResponse> getPostList(final String sortBy,
                                            final Pageable pageable) {

        final SortType sortType = validateSortType(sortBy.toUpperCase());

        return this.postQueryRepository.postList(pageable, sortType);
    }

    private SortType validateSortType(final String sortBy) {
        for (SortType value : SortType.values()) {
            if (value.getSortType().equalsIgnoreCase(sortBy.toUpperCase())) {
                return value;
            }
        }
        throw new RuntimeException("없어");
    }
}