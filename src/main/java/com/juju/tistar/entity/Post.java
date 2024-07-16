package com.juju.tistar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column
    private String content;

    @BatchSize(size = 300)
    @OneToMany(mappedBy = "post")
    private Set<Heart> hearts = new HashSet<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Image> Images = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Review> reviews = new HashSet<>();
    public Post(
            final String content,
            final User user) {
        this.content = content;
        setUser(user);
    }
}
