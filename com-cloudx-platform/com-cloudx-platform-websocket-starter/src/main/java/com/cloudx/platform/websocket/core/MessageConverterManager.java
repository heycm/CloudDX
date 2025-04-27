package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.MessageType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息转换器管理
 * @author heycm
 * @version 1.0
 * @since 2025/4/28 0:03
 */
public class MessageConverterManager {

    private final Map<MessageType, MessageConverter> converters = new ConcurrentHashMap<>();


    public MessageConverter getConverter(MessageType messageType) {
        return converters.get(messageType);
    }
}
