package com.juju.tistar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juju.tistar.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

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
        private String pwd;

        private String profile;

        private List<Role> roles;

}
