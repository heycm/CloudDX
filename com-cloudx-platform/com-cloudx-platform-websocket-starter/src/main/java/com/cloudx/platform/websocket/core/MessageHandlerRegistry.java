package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.BaseMessage;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器注册中心
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:12
 */
public class MessageHandlerRegistry {

    private final Map<String, MessageHandler<BaseMessage>> handlers = new ConcurrentHashMap<>();

    public MessageHandlerRegistry(List<MessageHandler<BaseMessage>> handlerList) {
        handlerList.forEach(this::addHandler);
    }

    public void addHandler(MessageHandler<BaseMessage> handler) {
        if (handlers.containsKey(handler.getSupportedMessageType())) {
            throw new IllegalStateException("Duplicate handler for type: "
                    + handler.getSupportedMessageType());
        }
        handlers.put(handler.getSupportedMessageType(), handler);
    }

    public Optional<MessageHandler<BaseMessage>> getHandler(String messageType) {
        return Optional.ofNullable(handlers.get(messageType));
    }
}
