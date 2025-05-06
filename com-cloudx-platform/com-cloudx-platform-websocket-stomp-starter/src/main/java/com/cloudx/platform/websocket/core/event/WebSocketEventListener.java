package com.cloudx.platform.websocket.core.event;

import com.cloudx.platform.websocket.core.repository.GroupRepository;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

/**
 * 事件监听
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 22:54
 */
public class WebSocketEventListener {

    private final SessionRepository sessionRepository;
    private final GroupRepository groupRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(SessionRepository sessionRepository, GroupRepository groupRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.sessionRepository = sessionRepository;
        this.groupRepository = groupRepository;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @EventListener
    public void onSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String userId = (String) accessor.getSessionAttributes().get("USER_ID");
        sessionRepository.save(new SessionWrapper(sessionId, userId));
    }

    /**
     * 监听会话断开事件
     * @param event
     */
    @EventListener
    public void onSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        sessionRepository.removeSession(sessionId);
        groupRepository.leaveAll(sessionId);
    }
}
