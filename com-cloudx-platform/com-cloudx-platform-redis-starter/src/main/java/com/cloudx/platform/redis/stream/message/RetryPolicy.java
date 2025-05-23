package com.cloudx.platform.redis.stream.message;

/**
 * 重试策略
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:19
 */
public interface RetryPolicy {

    int getMaxAttempts();

    boolean shouldRetry(Exception ex);
}
