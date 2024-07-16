package com.juju.tistar.service;

import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.Review;
import com.juju.tistar.entity.User;
import com.juju.tistar.exception.HttpException;
import com.juju.tistar.repository.PostRepository;
import com.juju.tistar.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public void deleteReview(final Long reviewId) {
        User user = userService.getCurrentUser();
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없음"));

        if(!user.getName().equals(review.getUser().getName()))
            throw new HttpException(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다.");
        reviewRepository.delete(review);
    }
}
