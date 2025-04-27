package com.cloudx.platform.websocket.converter;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.core.MessageConverter;
import com.cloudx.platform.websocket.model.message.JsonMessage;
import com.cloudx.platform.websocket.model.message.MessageType;

/**
 * Json消息转换器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 23:08
 */
public class JsonMessageConverter implements MessageConverter<JsonMessage> {

    @Override
    public MessageType getSupportedType() {
        return MessageType.JSON;
    }

    @Override
    public String serialize(JsonMessage message) {
        return Jackson.toJson(message);
    }

    @Override
    public JsonMessage deserialize(String message) {
        return Jackson.toObject(message, JsonMessage.class);
    }
}
