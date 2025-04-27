package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.BaseMessage;
import com.cloudx.platform.websocket.model.message.MessageType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息转换器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 23:06
 */
public abstract class MessageConverter<T extends BaseMessage> {

    private static final Map<MessageType, MessageConverter<?>> converters = new ConcurrentHashMap<>();

    public MessageConverter() {

    }

    /**
     * 序列化
     * @param message 消息
     * @return 序列化后的消息
     */
    public abstract String serialize(T message);

    /**
     * 反序列化
     * @param message 消息
     * @return 反序列化后的消息
     */
    public abstract T deserialize(String message);

}
