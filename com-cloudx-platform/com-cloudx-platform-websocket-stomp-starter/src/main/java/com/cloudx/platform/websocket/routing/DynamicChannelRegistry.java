package com.cloudx.platform.websocket.routing;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 动态通道注册服务
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 22:10
 */
public class DynamicChannelRegistry {

    private static final String ACTIVE_CHANNELS_KEY = "websocket:active:channels";

    private static final int ClEAN_INTERVAL = 60 * 1000;

    private final RedisTemplate<String, Object> redisTemplate;

    public DynamicChannelRegistry(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 注册通道
     * @param channelId 通道ID
     * @param userId    用户ID
     */
    public void registerChannel(String channelId, String userId) {
        redisTemplate.opsForHash().put(ACTIVE_CHANNELS_KEY, channelId, userId);
        redisTemplate.expire(channelId, 30, TimeUnit.MINUTES);
    }

    /**
     * 注销通道
     * @param channelId 通道ID
     */
    public void removeChannel(String channelId) {
        redisTemplate.opsForHash().delete(ACTIVE_CHANNELS_KEY, channelId);
    }

    /**
     * 获取通道归属用户ID
     * @param channelId 通道ID
     * @return 用户ID
     */
    public Optional<String> resolveUser(String channelId) {
        return Optional.ofNullable((String) redisTemplate.opsForHash().get(ACTIVE_CHANNELS_KEY, channelId));
    }

    /**
     * 定时任务，清理过期的通道
     */
    @Scheduled(fixedRate = ClEAN_INTERVAL)
    public void cleanExpiredChannels() {
        Set<Object> channels = redisTemplate.opsForHash().keys(ACTIVE_CHANNELS_KEY);
        channels.forEach(channelId -> {
            if (!redisTemplate.hasKey((String) channelId)) {
                redisTemplate.opsForHash().delete(ACTIVE_CHANNELS_KEY, channelId);
            }
        });
    }
}
