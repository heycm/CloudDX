package com.cloudx.platform.redis.limiter;

import com.cloudx.common.tools.UUIDUtil;
import com.cloudx.platform.redis.client.RedisClient;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * 固定窗口限流
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:13
 */
@Slf4j
public class FixWindowRateLimiter implements IRateLimiter {

    private static final String PREFIX = "rate:limiter:fix:window:";
    private static final DefaultRedisScript<Long> SCRIPT;
    private final RedisClient redisClient;

    static {
        String lua = """
                local request_count = redis.call('get', KEYS[1])
                if request_count and tonumber(request_count) >= tonumber(ARGV[1]) then
                    return 0
                else if not request_count then
                    redis.call('set', KEYS[1], 1, 'EX', ARGV[2])
                else
                    redis.call('incr', KEYS[1])
                end
                return 1
                """;
        SCRIPT = new DefaultRedisScript<>(lua, Long.class);
    }

    public FixWindowRateLimiter(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public boolean allowRequest(String key, int limit, int window, String requestId) {
        List<String> keys = Collections.singletonList(PREFIX + key);
        Long result = redisClient.execute(SCRIPT, keys, limit, window);
        boolean allowed = result != null && result.equals(1L);
        if (log.isDebugEnabled()) {
            log.debug("FixWindowRateLimiter key [{}] request [{}] allowed: {}", keys, requestId, allowed);
        }
        return allowed;
    }

    @Override
    public boolean allowRequest(String key, int limit, int window) {
        return allowRequest(key, limit, window, UUIDUtil.getId());
    }
}
