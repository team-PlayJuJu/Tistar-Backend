package com.juju.tistar.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class QuerydslConfig {
    private final EntityManager em;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
}