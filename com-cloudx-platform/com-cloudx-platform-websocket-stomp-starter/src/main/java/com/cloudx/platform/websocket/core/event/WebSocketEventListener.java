package com.cloudx.platform.websocket.core.event;

import com.cloudx.platform.websocket.core.repository.GroupRepository;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

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
        String channelId = (String) accessor.getSessionAttributes().get("CHANNEL_ID");
        String userId = (String) accessor.getSessionAttributes().get("USER_ID");
        if (channelId != null) {
            // 保存会话
            sessionRepository.save(new SessionWrapper(channelId, userId));
            // 发送channelId到客户端专用队列
            String destination = String.format("/user/%s/queue/channelInfo", channelId);
            messagingTemplate.convertAndSend(destination, Map.of(
                    "type", "CONNECTION_ESTABLISHED",
                    "channelId", channelId
            ));
        }
    }

    /**
     * 监听会话断开事件
     * @param event
     */
    @EventListener
    public void onSessionDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String channelId = (String) accessor.getSessionAttributes().get("CHANNEL_ID");
        if (channelId != null) {
            sessionRepository.removeChannel(channelId);
            groupRepository.leaveAll(channelId);
        }
    }
}
