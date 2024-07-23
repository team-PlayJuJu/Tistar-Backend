package com.juju.tistar.repository;

import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByName(String name);
    boolean existsUserByName(String name);

    @Query("SELECT post FROM Post post " +
            "JOIN FETCH post.user " +
            "WHERE post.user.id = :userId " +
            "ORDER BY post.createdAt DESC")
    Slice<Post> findAllMyPosts(@Param("userId") final Long userId, final Pageable pageable);

    @Query("SELECT post FROM Post post" +
            " JOIN post.hearts hearts" +
            " JOIN FETCH post.user user" +
            " WHERE hearts.user.id = :id" +
            " ORDER BY post.createdAt DESC")
    Slice<Post> findAllMyLikePosts(@Param("id") final Long userId, final Pageable pageable);
}