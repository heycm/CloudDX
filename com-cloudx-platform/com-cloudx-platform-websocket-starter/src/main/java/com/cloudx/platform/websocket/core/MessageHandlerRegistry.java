package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.MessageType;

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

    private final Map<MessageType, MessageHandler<?>> handlers = new ConcurrentHashMap<>();

    public MessageHandlerRegistry(List<MessageHandler<?>> handlerList) {
        handlerList.forEach(this::addHandler);
    }

    public void addHandler(MessageHandler<?> handler) {
        if (handlers.containsKey(handler.getSupportedType())) {
            throw new IllegalStateException("Duplicate handler for type: "
                    + handler.getSupportedType());
        }
        handlers.put(handler.getSupportedType(), handler);
    }

    public Optional<MessageHandler<?>> getHandler(MessageType messageType) {
        return Optional.ofNullable(handlers.get(messageType));
    }
}
