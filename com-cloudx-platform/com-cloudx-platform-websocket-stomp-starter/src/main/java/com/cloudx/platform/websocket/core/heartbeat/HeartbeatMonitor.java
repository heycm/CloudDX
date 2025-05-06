package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;

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

    @MessageMapping("/app/ping")
    @SendToUser("/queue/pong")
    public String ping(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        SessionWrapper session = sessionRepository.getSession(sessionId);
        if (session != null) {
            session.updateHeartbeat();
            sessionRepository.save(session);
        }
        return "pong";
    }
}
