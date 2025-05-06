package com.cloudx.platform.websocket.core.service;

import com.cloudx.platform.websocket.core.repository.GroupRepository;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 消息服务实现
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:54
 */
public class MessagingServiceImpl implements MessagingService {

    private final SessionRepository sessionRepository;
    private final GroupRepository  groupRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessagingServiceImpl(SessionRepository sessionRepository, GroupRepository groupRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.sessionRepository = sessionRepository;
        this.groupRepository = groupRepository;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void ping(String sessionId) {
        messagingTemplate.convertAndSendToUser(sessionId, "/heartbeat", "ping");
    }

    @Override
    public void pong(String sessionId) {
        messagingTemplate.convertAndSendToUser(sessionId, "/heartbeat", "pong");
    }
}
