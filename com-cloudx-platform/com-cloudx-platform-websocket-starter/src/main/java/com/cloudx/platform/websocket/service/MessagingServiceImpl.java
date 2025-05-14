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

    private final GroupRepository groupRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessagingServiceImpl(GroupRepository groupRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.groupRepository = groupRepository;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendTo(String sessionId, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(sessionId, destination, payload);
    }

    @Override
    public void sendGroup(String senderId, String groupId, String destination, Object payload) {
        Set<String> members = groupRepository.getGroupMembers(groupId);
        for (String member : members) {
            if (!member.equals(senderId)) {
                VirtualThread.execute(() -> {
                    sendTo(member, destination, payload);
                });
            }
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
