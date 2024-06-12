package com.juju.tistar.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
@Entity
@Getter
@Setter
@Table(name = "likes")
@NoArgsConstructor
public class Heart {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Heart(final User user,final Post post) {
        setUser(user);
        setPost(post);
    }
    public void delete() {
        this.user.getHearts().remove(this);
        this.post.getHearts().remove(this);
    }
}

