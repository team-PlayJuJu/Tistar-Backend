package com.juju.tistar.repository;
import com.juju.tistar.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Query("SELECT heart FROM Heart heart WHERE heart.user.id = :userId AND heart.post.id = :postId")
    Optional<Heart> findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Heart heart WHERE heart.user.id = :userId AND heart.post.id = :postId")
    int deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    @Query("SELECT count(heart) FROM Heart heart WHERE heart.post.id = :postId")
    int countByPostId(@Param("postId") Long postId);

    @Query("SELECT EXISTS(SELECT heart FROM Heart heart WHERE heart.user.id = :userId AND heart.post.id = :postId)")
    boolean existsByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}
