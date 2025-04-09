package com.cloudx.platform.apisign.repository.impl;

import com.cloudx.platform.apisign.repository.NonceRepository;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 随机字符存储实现
 * @author heycm
 * @version 1.0
 * @since 2025/4/9 22:24
 */
public class NonceRepositoryImpl implements NonceRepository {

    private static final String NONCE_KEY = ":apisign:nonce:";

    private final RedisTemplate<String, Object> redisTemplate;

    public NonceRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean saveIfAbsent(String tenantId, String nonce, long timeout, TimeUnit timeUnit) {
        String key = tenantId + NONCE_KEY + nonce;
        Boolean set = redisTemplate.boundValueOps(key).setIfAbsent(1, timeout, timeUnit);
        return Boolean.TRUE.equals(set);
    }
}
