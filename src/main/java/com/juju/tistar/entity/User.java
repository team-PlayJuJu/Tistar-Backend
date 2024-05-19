package com.juju.tistar.entity;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String Image;
}
