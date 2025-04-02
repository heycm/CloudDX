package com.cloudx.platform.redis.limiter;

/**
 * 限流器接口
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:04
 */
public interface IRateLimiter {

    /**
     * 是否允许请求
     * @param key       限流Key
     * @param limit     时间窗口内允许通过数量
     * @param window    时间窗口（秒）
     * @param requestId 请求唯一ID
     * @return true-是
     */
    boolean allowRequest(String key, int limit, int window, String requestId);

    /**
     * 是否允许请求
     * @param key    限流Key
     * @param limit  时间窗口内允许通过数量
     * @param window 时间窗口（秒）
     * @return true-是
     */
    boolean allowRequest(String key, int limit, int window);

}
