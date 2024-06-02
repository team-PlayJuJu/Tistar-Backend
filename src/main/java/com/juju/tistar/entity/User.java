package com.juju.tistar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.Set;

@Table(name = "user")
@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class User {
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
        @GenericGenerator(name = "native",strategy = "native")
        @Column(name = "user_id")
        private Long id;

        @Column
        private String name;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @Column
        private String pwd;

        @Column
        private String profile;

        @JsonIgnore
        @OneToMany(mappedBy="user",fetch=FetchType.EAGER)
        private Set<Authority> authorities;
}
