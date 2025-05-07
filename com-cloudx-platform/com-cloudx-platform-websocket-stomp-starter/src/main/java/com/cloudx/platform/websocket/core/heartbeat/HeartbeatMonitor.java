package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import com.cloudx.platform.websocket.core.session.WebSocketSessionLocalStorage;
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

    private static final String SESSION_CLOSE_QUEUE = "websocket:session:close:queue";

    private final SessionRepository sessionRepository;

    private final WebSocketProperties.Heartbeat heartbeatConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    private final SimpMessagingTemplate messagingTemplate;

    public HeartbeatMonitor(SessionRepository sessionRepository, WebSocketProperties.Heartbeat heartbeat, RedisTemplate<String, Object> redisTemplate, SimpMessagingTemplate simpMessagingTemplate) {
        this.sessionRepository = sessionRepository;
        this.heartbeatConfig = heartbeat;
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = simpMessagingTemplate;
        this.startSessionCloseListener();
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

    private void handleExpiredSession(SessionWrapper session, long currentTime) {
        if (session.getPingAttempts() >= heartbeatConfig.getMaxRetries() || currentTime - session.getLastHeartbeatTime() >= heartbeatConfig.getMaxInterval()) {
            closeSession(session);
        } else {
            pingSession(session);
            session.incrementPingAttempt();
            sessionRepository.save(session);
        }
    }

    private void closeSession(SessionWrapper session) {
        boolean closed = WebSocketSessionLocalStorage.close(session.getSessionId());
        if (closed) {
            sessionRepository.removeSession(session.getSessionId());
        } else {
            redisTemplate.convertAndSend(SESSION_CLOSE_QUEUE, session.getSessionId());
        }
    }

    private void pingSession(SessionWrapper session) {
        messagingTemplate.convertAndSendToUser(session.getUserId(), "/user/ping", "ping");
    }

    private void startSessionCloseListener() {
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            String sessionId = new String(message.getBody());
            boolean closed = WebSocketSessionLocalStorage.close(sessionId);
            if (closed) {
                sessionRepository.removeSession(sessionId);
            }
        }, SESSION_CLOSE_QUEUE.getBytes());
    }
}
