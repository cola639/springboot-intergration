package org.spring.springboot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisHealthChecker {

    private final StringRedisTemplate redisTemplate;

    public boolean isAvailable() {
        try {
            redisTemplate.opsForValue().set("redis:ping:test", "pong", Duration.ofSeconds(5));
            return "pong".equals(redisTemplate.opsForValue().get("redis:ping:test"));
        } catch (Exception e) {
            return false;
        }
    }
}
