package com.cloudx.platform.redis.limiter;

import com.cloudx.common.tools.UUIDUtil;
import com.cloudx.platform.redis.client.RedisClient;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * 滑动窗口限流
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:05
 */
@Slf4j
public class SlidingWindowRateLimiter implements IRateLimiter {

    private static final String PREFIX = "rate:limiter:sliding:window:";
    private static final DefaultRedisScript<Long> SCRIPT;
    private final RedisClient redisClient;

    static {
        String lua = """
                local key = KEYS[1]
                local current_time = tonumber(ARGV[1])
                local window = tonumber(ARGV[2])
                local limit = tonumber(ARGV[3])
                local request_id = ARGV[4]
                local window_start = current_time - window * 1000
                redis.call('ZREMRANGEBYSCORE', key, '-inf', window_start)
                local request_count = redis.call('ZCARD', key)
                if request_count and request_count >= limit then
                    return 0
                else
                    redis.call('ZADD', key, current_time, request_id)
                    redis.call('EXPIRE', key, window)
                    return 1
                end
                """;
        SCRIPT = new DefaultRedisScript<>(lua, Long.class);
    }

    public SlidingWindowRateLimiter(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public boolean allowRequest(String key, int limit, int window, String requestId) {
        List<String> keys = Collections.singletonList(PREFIX + key);
        long currentTime = System.currentTimeMillis();
        Long result = redisClient.execute(SCRIPT, keys, currentTime, window, limit, requestId);
        boolean allowed = result != null && result.equals(1L);
        if (log.isDebugEnabled()) {
            log.debug("SlidingWindowRateLimiter key [{}] request [{}] allowed: {}", keys, requestId, allowed);
        }
        return allowed;
    }

    @Override
    public boolean allowRequest(String key, int limit, int window) {
        return allowRequest(key, limit, window, UUIDUtil.getId());
    }
}
