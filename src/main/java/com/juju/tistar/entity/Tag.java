package com.juju.tistar.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<Post> boards;

    public Tag(final String name) {
        this.name = name;
        this.boards = new HashSet<>();
    }

}