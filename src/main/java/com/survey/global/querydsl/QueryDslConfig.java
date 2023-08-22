package com.survey.global.querydsl;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    @PersistenceContext
    protected EntityManager entityManager;

    @Bean
    public JPAQueryFactory query() {
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }
}
