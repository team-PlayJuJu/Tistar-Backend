package com.juju.tistar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.processing.Generated;
import java.util.Set;

@Table(name = "User")
@Entity
@Getter
@Setter
public class User {
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
        @GenericGenerator(name = "native",strategy = "native")
        @Column(name = "user_id")
        private int id;

        private String name;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String pwd;

        @Column(name = "create_dt")
        private String createDt;

        @JsonIgnore
        @OneToMany(mappedBy="user",fetch=FetchType.EAGER)
        private Set<Authority> authorities;
}
