package com.cloudx.platform.redis.stream.message;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:21
 */
public class DefaultRetryPolicy implements RetryPolicy {

    private final int maxAttempts;

    public DefaultRetryPolicy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public int getMaxAttempts() {
        return maxAttempts;
    }

    @Override
    public boolean shouldRetry(Exception ex) {
        return false;
    }
}
