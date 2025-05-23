package com.cloudx.platform.redis.client;

import com.cloudx.common.entity.tenant.TenantContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.StringUtils;

public class TenantRedisClientImpl extends RedisClientImpl {

    public TenantRedisClientImpl(RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer listenerContainer) {
        super(redisTemplate, listenerContainer);
    }

    @Override
    protected String wrapKey(String key) {
        String tenantId = TenantContextHolder.getTenantId();
        return StringUtils.hasText(tenantId) ? tenantId + ":" + key : key;
    }
}
