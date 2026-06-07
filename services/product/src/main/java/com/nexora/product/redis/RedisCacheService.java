package com.nexora.product.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T get(String key,
                     Object hashKey,
                     Class<T> clazz) {

        Object value =
                redisTemplate.opsForHash()
                        .get(key, hashKey);

        return objectMapper.convertValue(
                value,
                clazz
        );
    }

    public Boolean isHasKeyExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Long delete(String key,String hashKey){
        return  redisTemplate.opsForHash().delete(key,hashKey);
    }

    public void put(String key,
                    Object hashKey,
                    Object value) {

        redisTemplate.opsForHash()
                .put(key, hashKey, value);
    }
}