package com.juju.tistar.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juju.tistar.entity.enums.Role;
import com.juju.tistar.util.StringListConverter;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "user")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long id;

        private String name;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        private String introduction;

        @Convert(converter = StringListConverter.class)
        private List<Role> roles;

        @OneToMany(mappedBy = "user")
        @Builder.Default
        @JsonBackReference
        private Set<Heart> hearts = new HashSet<>();

        @OneToMany(mappedBy = "user")
        @Builder.Default
        @JsonBackReference
        private Set<Post> posts = new HashSet<>();

        @OneToMany(mappedBy = "user")
        @Builder.Default
        @JsonBackReference
        private Set<Review> reviews = new HashSet<>();
}
