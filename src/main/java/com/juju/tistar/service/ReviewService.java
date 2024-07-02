package com.juju.tistar.service;

import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.Review;
import com.juju.tistar.entity.User;
import com.juju.tistar.repository.PostRepository;
import com.juju.tistar.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewService  {
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public void createReview(Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userService.getCurrentUser();
        Review review = Review.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
        reviewRepository.save(review);
    }
}
