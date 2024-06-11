package com.juju.tistar.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Setter
@Table(name = "Image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String imagePath;
    public Image(
            final String fileName,
            final String storeImagePath) {
        this.fileName = fileName;
        this.imagePath = storeImagePath;
    }
}