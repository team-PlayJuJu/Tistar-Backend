package com.juju.tistar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column
    private Long heart;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = LAZY)
    private Tag tag;

    @OneToMany(mappedBy = "post")
    private Set<Image> Images = new HashSet<>();
}