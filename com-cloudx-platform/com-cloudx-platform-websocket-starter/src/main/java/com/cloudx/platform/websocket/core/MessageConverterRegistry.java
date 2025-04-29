package com.cloudx.platform.websocket.core;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.model.message.BaseMessage;
import com.cloudx.platform.websocket.model.message.MessageType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息转换器注册中心
 * @author heycm
 * @version 1.0
 * @since 2025/4/28 0:03
 */
public class MessageConverterRegistry {

    private final Map<String, MessageConverter<BaseMessage>> converters = new ConcurrentHashMap<>();

    public MessageConverterRegistry(List<MessageConverter<BaseMessage>> converters) {
        converters.forEach(this::addConverter);
    }

    public void addConverter(MessageConverter<BaseMessage> converter) {
        if (converters.containsKey(converter.getSupportedMessageType())) {
            throw new IllegalStateException("Duplicate handler for type: "
                    + converter.getSupportedMessageType());
        }
        converters.put(converter.getSupportedMessageType(), converter);
    }

    public MessageConverter<BaseMessage> getConverter(String message) {
        MessageConverter<BaseMessage> converter = converters.get(message);
        if (converter == null) {
            BaseMessage object = Jackson.toObject(message, BaseMessage.class);
            if (object != null) {
                converter = converters.get(object.getMessageType());
            }
        }
        if (converter == null) {
            converter = converters.get(MessageType.UNKNOWN.name());
        }
        return converter;
    }
}
