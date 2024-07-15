package com.juju.tistar.service;

import com.juju.tistar.entity.Heart;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.mapper.HeartMapper;
import com.juju.tistar.repository.HeartQueryRepository;
import com.juju.tistar.repository.HeartRepository;
import com.juju.tistar.repository.PostRepository;
import com.juju.tistar.request.HeartRequest;
import com.juju.tistar.response.CancelHeartResponse;
import com.juju.tistar.response.GetHeartResponse;
import com.juju.tistar.response.HeartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final  UserService userService;
    private final PostRepository postRepository;
    private final HeartQueryRepository heartQueryRepository;


    @Transactional
    public HeartResponse registerHeart(final Long postId) {

        User user = userService.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물 못찾음"));

        Heart heart = HeartMapper.HeartRequest(user, post);

        heartRepository.save(heart);

        return HeartMapper.heartResponse(heart);
    }

    @Transactional
    public CancelHeartResponse cancelHeart(final Long postId) {
        User user = userService.getCurrentUser();
        Heart heart = heartRepository.findByUserIdAndPostId(user.getId(), postId)
                .orElseThrow(() -> new RuntimeException("하트를 누르지 않음"));

        heart.delete();
        heartRepository.deleteById(heart.getId());

        return new CancelHeartResponse(heart.getId());
    }

    public GetHeartResponse getHeart(final Long postId) {

        final boolean isExistsPost = postRepository.existsById(postId);
        if (!isExistsPost) {
            throw new RuntimeException("게시물을 찾을 수 없음");
        }

        User user = userService.getCurrentUser();
        boolean isHeart = generateIsHeart(user.getId(),postId);

        int heartCount = Math.toIntExact(heartQueryRepository.countByPostId(postId));

        return new GetHeartResponse(isHeart, heartCount);
    }
    private boolean generateIsHeart(final Long userId, final Long postId) {
        return heartRepository.existsByUserIdAndPostId(userId, postId);
    }
}
