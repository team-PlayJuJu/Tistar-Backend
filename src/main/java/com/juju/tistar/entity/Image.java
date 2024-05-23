package com.juju.tistar.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "boardImage")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column(nullable = false)
    private String image;

    @Column
    private String content;

    @Column
    private Long heart;

    @Column
    private String tag;

}