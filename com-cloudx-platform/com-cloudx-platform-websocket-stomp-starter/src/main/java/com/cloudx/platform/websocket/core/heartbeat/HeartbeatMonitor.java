package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 心跳监控
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 23:05
 */
public class HeartbeatMonitor {

    private final SessionRepository sessionRepository;

    private final WebSocketProperties.Heartbeat heartbeat;

    public HeartbeatMonitor(SessionRepository sessionRepository, WebSocketProperties.Heartbeat heartbeat) {
        this.sessionRepository = sessionRepository;
        this.heartbeat = heartbeat;
    }
}
