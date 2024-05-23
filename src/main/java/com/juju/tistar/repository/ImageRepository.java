package com.juju.tistar.repository;

import com.juju.tistar.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>{
    public List<Image> findAllByImageId(Long Id);
}
