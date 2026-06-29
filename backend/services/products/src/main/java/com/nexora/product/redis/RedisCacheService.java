package com.nexora.product.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T get(String key, Object hashKey, Class<T> clazz) {
        log.debug("Fetching from Redis - Key: {}, HashKey: {}", key, hashKey);

        Object value = redisTemplate.opsForHash().get(key, hashKey);

        log.trace("Cache hit for Key: {}, HashKey: {}", key, hashKey);
        return objectMapper.convertValue(value, clazz);
    }

    public Boolean isHasKeyExists(String key, String hashKey) {
        Boolean exists = redisTemplate.opsForHash().hasKey(key, hashKey);
        log.trace("Checking existence for Key: {}, HashKey: {} - Exists: {}", key, hashKey, exists);
        return exists;
    }

    public Long delete(String key, String hashKey) {
        log.info("Deleting entry from Redis - Key: {}, HashKey: {}", key, hashKey);
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    public void put(String key, Object hashKey, Object value) {
        log.debug("Putting entry into Redis - Key: {}, HashKey: {}", key, hashKey);
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
}