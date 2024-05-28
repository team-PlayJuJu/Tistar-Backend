package com.juju.tistar.repository;

import com.juju.tistar.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    public List<Post> findAllByPost(Long Id);
}
