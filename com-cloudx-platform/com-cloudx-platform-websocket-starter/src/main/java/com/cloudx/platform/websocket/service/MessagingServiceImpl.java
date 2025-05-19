package com.cloudx.platform.websocket.service;

import com.cloudx.common.tools.threadpool.virtual.VirtualThread;
import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.repository.GroupRepository;
import com.cloudx.platform.websocket.repository.SessionRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Set;

/**
 * 消息服务实现
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:54
 */
public class MessagingServiceImpl implements MessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    public MessagingServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.messagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendToUser(String user, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(user, destination, payload);
    }

    @Override
    public void sendToUsers(Set<String> users, String destination, Object payload) {
        for (String user : users) {
            VirtualThread.execute(() -> {
                sendToUser(user, destination, payload);
            });
        }
    }

    @Override
    public void sendTopic(String topicId, String destinationPrefix, Object payload) {
        destinationPrefix = destinationPrefix.startsWith("/") ? destinationPrefix : "/" + destinationPrefix;
        messagingTemplate.convertAndSend(destinationPrefix + "/" + topicId, payload);
    }

    @Override
    public void sendBroadcast(Object payload) {
        messagingTemplate.convertAndSend(ServerConstant.BROADCAST_DESTINATION, payload);
    }

    @Override
    public void sendBroadcast(String destinationSuffix, Object payload) {
        messagingTemplate.convertAndSend(ServerConstant.BROADCAST_DESTINATION + "/" + destinationSuffix, payload);
    }
}
