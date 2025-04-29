package com.cloudx.platform.websocket.dispatcher;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.core.MessageDispatcher;
import com.cloudx.platform.websocket.core.MessageHandler;
import com.cloudx.platform.websocket.core.MessageHandlerRegistry;
import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.core.SessionWrapper;
import com.cloudx.platform.websocket.model.message.BaseMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Optional;

/**
 * Redis发布订阅模式消息分发
 * @author heycm
 * @version 1.0
 * @since 2025/4/29 18:39
 */
public class RedisMessageDispatcher implements MessageDispatcher, MessageListener {

    public static final String CHANNEL = "websocket:message:dispatcher";

    private final SessionRepository sessionRepository;
    private final MessageHandlerRegistry messageHandlerRegistry;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageDispatcher(SessionRepository sessionRepository, MessageHandlerRegistry messageHandlerRegistry, RedisTemplate<String, Object> redisTemplate) {
        this.sessionRepository = sessionRepository;
        this.messageHandlerRegistry = messageHandlerRegistry;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void dispatch(BaseMessage message) {
        redisTemplate.convertAndSend(CHANNEL, message);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        String body = serializer.deserialize(message.getBody());
        BaseMessage data = Jackson.toObject(body, BaseMessage.class);
        if (data == null) {
            return;
        }

        Optional<SessionWrapper> sessionWrapperOptional = sessionRepository.getBySessionId(data.getCurrentSessionId());
        if (!sessionWrapperOptional.isPresent()) {
            return;
        }
        Optional<MessageHandler<BaseMessage>> handlerOptional = messageHandlerRegistry.getHandler(data.getMessageType());
        if (!handlerOptional.isPresent()) {
            return;
        }
        handlerOptional.get().handleMessage(data, sessionWrapperOptional.get());
    }
}
