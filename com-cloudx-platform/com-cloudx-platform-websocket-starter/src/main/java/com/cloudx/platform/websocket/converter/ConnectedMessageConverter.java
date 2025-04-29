package com.cloudx.platform.websocket.converter;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.core.MessageConverter;
import com.cloudx.platform.websocket.model.message.ConnectedMessage;
import com.cloudx.platform.websocket.model.message.MessageType;

/**
 * 建立连接成功消息转换器
 * @author heycm
 * @version 1.0
 * @since 2025/4/29 18:16
 */
public class ConnectedMessageConverter implements MessageConverter<ConnectedMessage> {

    @Override
    public String getSupportedMessageType() {
        return MessageType.CONNECTED.name();
    }

    @Override
    public String serialize(ConnectedMessage message) {
        return Jackson.toJson(message);
    }

    @Override
    public ConnectedMessage deserialize(String message) {
        return Jackson.toObject(message, ConnectedMessage.class);
    }
}
