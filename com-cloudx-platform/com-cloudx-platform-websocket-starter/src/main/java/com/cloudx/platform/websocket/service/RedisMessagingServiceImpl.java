package com.cloudx.platform.websocket.service;

import com.cloudx.common.tools.Jackson;
import com.cloudx.common.tools.threadpool.virtual.VirtualThread;
import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.core.session.WebSocketSessionLocalStorage;
import com.cloudx.platform.websocket.message.MassMessage;
import com.cloudx.platform.websocket.message.Message;
import com.cloudx.platform.websocket.message.TopicMessage;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Redis中转消息
 * @author heycm
 * @version 1.0
 * @since 2025/5/19 21:27
 */
public class RedisMessagingServiceImpl implements MessagingService {

    private static final String USER_MESSAGE_QUEUE = "websocket:message:user:queue";
    private static final String MASS_MESSAGE_QUEUE = "websocket:message:mass:queue";
    private static final String TOPIC_MESSAGE_QUEUE = "websocket:message:topic:queue";
    private static final String BROADCAST_MESSAGE_QUEUE = "websocket:message:broadcast:queue";

    private final RedisTemplate<String, Object> redisTemplate;

    private final SimpMessagingTemplate messagingTemplate;


    public RedisMessagingServiceImpl(RedisTemplate<String, Object> redisTemplate, SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendToUser(String user, String destination, Object payload) {
        VirtualThread.execute(() -> {
            if (WebSocketSessionLocalStorage.exists(user)) {
                messagingTemplate.convertAndSendToUser(user, destination, payload);
            } else {
                Message message = new Message(user, destination, payload);
                redisTemplate.convertAndSend(USER_MESSAGE_QUEUE, Jackson.toJson(message));
            }
        });
    }

    @Override
    public void sendToUsers(Set<String> users, String destination, Object payload) {
        Set<String> localUsers = users.stream().filter(WebSocketSessionLocalStorage::exists).collect(Collectors.toSet());
        localUsers.forEach(user -> {
            VirtualThread.execute(() -> {
                messagingTemplate.convertAndSendToUser(user, destination, payload);
            });
        });
        users.removeAll(localUsers);
        if (!users.isEmpty()) {
            VirtualThread.execute(() -> {
                MassMessage message = new MassMessage(users, destination, payload);
                redisTemplate.convertAndSend(MASS_MESSAGE_QUEUE, Jackson.toJson(message));
            });
        }
    }

    @Override
    public void sendTopic(String topicId, String destinationPrefix, Object payload) {
        VirtualThread.execute(() -> {
            String prefix = destinationPrefix.startsWith("/") ? destinationPrefix : "/" + destinationPrefix;
            String topic = prefix + "/" + topicId;
            TopicMessage message = new TopicMessage(topic, payload);
            redisTemplate.convertAndSend(TOPIC_MESSAGE_QUEUE, Jackson.toJson(message));
        });
    }

    @Override
    public void sendBroadcast(Object payload) {
        VirtualThread.execute(() -> {
            TopicMessage message = new TopicMessage(ServerConstant.BROADCAST_DESTINATION, payload);
            redisTemplate.convertAndSend(BROADCAST_MESSAGE_QUEUE, Jackson.toJson(message));
        });
    }

    @Override
    public void sendBroadcast(String destinationSuffix, Object payload) {
        VirtualThread.execute(() -> {
            TopicMessage message = new TopicMessage(ServerConstant.BROADCAST_DESTINATION + "/" + destinationSuffix, payload);
            redisTemplate.convertAndSend(BROADCAST_MESSAGE_QUEUE, Jackson.toJson(message));
        });
    }

    @PostConstruct
    private void initMessageConsumer() {
        // 单发消息监听
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            Message data = Jackson.toObject(new String(message.getBody()), Message.class);
            if (data != null && WebSocketSessionLocalStorage.exists(data.getUser())) {
                VirtualThread.execute(() -> {
                    messagingTemplate.convertAndSendToUser(data.getUser(), data.getDestination(), data.getPayload());
                });
            }
        }, USER_MESSAGE_QUEUE.getBytes());

        // 群发消息监听
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            MassMessage data = Jackson.toObject(new String(message.getBody()), MassMessage.class);
            if (data != null) {
                data.getUsers().forEach(user -> {
                    if (WebSocketSessionLocalStorage.exists(user)) {
                        VirtualThread.execute(() -> {
                            messagingTemplate.convertAndSendToUser(user, data.getDestination(), data.getPayload());
                        });
                    }
                });
            }
        }, MASS_MESSAGE_QUEUE.getBytes());

        // 主题消息监听
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            TopicMessage data = Jackson.toObject(new String(message.getBody()), TopicMessage.class);
            if (data != null) {
                VirtualThread.execute(() -> {
                    messagingTemplate.convertAndSend(data.getDestination(), data.getPayload());
                });
            }
        }, TOPIC_MESSAGE_QUEUE.getBytes());

        // 广播消息监听
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            TopicMessage data = Jackson.toObject(new String(message.getBody()), TopicMessage.class);
            if (data != null) {
                VirtualThread.execute(() -> {
                    messagingTemplate.convertAndSend(data.getDestination(), data.getPayload());
                });
            }
        }, BROADCAST_MESSAGE_QUEUE.getBytes());
    }
}
