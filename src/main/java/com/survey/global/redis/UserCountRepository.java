package com.survey.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCountRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Long increment(String key) {
        return redisTemplate
                .opsForValue()
                .increment(key);
    }

    public void deleteAllByKey(String key) {
        redisTemplate.delete(key);
    }
}
