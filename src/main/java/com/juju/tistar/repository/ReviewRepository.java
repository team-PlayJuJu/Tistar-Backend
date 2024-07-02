package com.juju.tistar.repository;

import com.juju.tistar.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public List<Review> findAllByPostId(Long postId);
}
