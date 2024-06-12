package com.juju.tistar.repository;

import com.juju.tistar.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT images FROM Image images" +
            " WHERE images.id in :list")
    List<Image> findAllImagesById(@Param("list") final List<Long> imageIds);
}