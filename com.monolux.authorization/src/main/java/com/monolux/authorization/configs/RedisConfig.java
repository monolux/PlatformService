package com.monolux.authorization.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

import java.time.Duration;

// @EnableRedisHttpSession 을 사용하지 않고 RedisSessionRepository Bean 을 등록, SavedRequest 만 Session 에 저장하면 되기 때문
// @EnableRedisHttpSession
@EnableSpringHttpSession
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisOperations<String, Object> sessionRedisOperations(final RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisSessionRepository sessionRepository(final RedisOperations<String, Object> sessionRedisOperations) {
        RedisSessionRepository redisSessionRepository = new RedisSessionRepository(sessionRedisOperations);
        redisSessionRepository.setDefaultMaxInactiveInterval(Duration.ofSeconds(300));
        return redisSessionRepository;
    }

    // AWS Elastic Cache 에서 config Method 를 막아서 추가한 코드
    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}