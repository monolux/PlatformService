package com.monolux.domain.configs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class QueryDslConfig {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @PersistenceContext
    private final EntityManager entityManager;

    //endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    QueryDslConfig(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(this.entityManager);
    }

    // endregion
}