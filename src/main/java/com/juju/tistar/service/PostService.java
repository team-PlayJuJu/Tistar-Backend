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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PostQueryRepository postQueryRepository;
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
    public WritePostResponse writePost(final WritePostRequest request, final Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 없음"));

        final Post post = new Post();
        post.setUser(user);
        postRepository.save(post);

        mappingPostAndImage(request, post);

        return PostMapper.entityToWritePostResponse(post);
    }



    private void mappingPostAndImage(final WritePostRequest request, final Post post) {

        final List<Image> images = imageRepository.findAllImagesById(request.imageIds());
        images.forEach(image -> image.setPost(post));
    }

    @Transactional
    public PostDetailResponse readBoard(final Long boardId, final Long accessId) {

        final Post board = postRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없음"));

        return postQueryRepository.readPost(boardId, accessId);
    }

    public Slice<PostResponse> getBoardList(final String sortBy,
                                            final Pageable pageable,
                                            final String keyword) {

        final SortType sortType = validateSortType(sortBy.toUpperCase());

        return this.postQueryRepository.postList(pageable, sortType, keyword);
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