package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 心跳监控
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 23:05
 */
public class HeartbeatMonitor {

    private static final String HEARTBEAT_KEY = "websocket:heartbeat";

    private final RedisTemplate<String, Object> redisTemplate;

    private final WebSocketProperties.Heartbeat heartbeat;

    public HeartbeatMonitor(RedisTemplate<String, Object> redisTemplate, WebSocketProperties webSocketProperties) {
        this.redisTemplate = redisTemplate;
        this.heartbeat = webSocketProperties.getHeartbeat();
    }
}
