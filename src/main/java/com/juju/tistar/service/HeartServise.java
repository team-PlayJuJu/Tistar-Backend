package com.juju.tistar.service;

import com.juju.tistar.entity.Heart;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.mapper.HeartMapper;
import com.juju.tistar.repository.HeartQueryRepository;
import com.juju.tistar.repository.HeartRepository;
import com.juju.tistar.repository.PostRepository;
import com.juju.tistar.repository.UserRepository;
import com.juju.tistar.request.HeartRequest;
import com.juju.tistar.response.CancelHeartResponse;
import com.juju.tistar.response.GetHeartResponse;
import com.juju.tistar.response.HeartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartServise {
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HeartQueryRepository heartQueryRepository;


    @Transactional
    public HeartResponse registerHeart(final Long accessId, final HeartRequest request) {

        User user = userRepository.findById(accessId)
                .orElseThrow(() -> new RuntimeException("유저 못찾음"));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new RuntimeException("게시물 못찾음"));

        Heart heart = HeartMapper.HeartRequest(user, post);

        heartRepository.save(heart);

        return HeartMapper.heartResponse(heart);
    }

    @Transactional
    public CancelHeartResponse cancelLike(final Long userId, final Long postId) {
        Heart heart = heartRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("하트를 누르지 않음"));

        heart.delete();
        heartRepository.deleteById(heart.getId());

        return new CancelHeartResponse(heart.getId());
    }

    public GetHeartResponse getHeart(final Long userId, final Long postId) {

        final boolean isExistsBoard = postRepository.existsById(postId);
        if (!isExistsBoard) {
            throw new RuntimeException("게시물을 찾을 수 없음");
        }

        boolean isHeart = generateIsLike(userId, postId);

        int heartCount = Math.toIntExact(heartQueryRepository.countByPostId(postId));

        return new GetHeartResponse(isHeart, heartCount);
    }
    private boolean generateIsLike(final Long userId, final Long postId) {
        return heartRepository.existsByUserIdAndPostId(userId, postId);
    }
}
