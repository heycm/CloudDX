package com.cloudx.platform.websocket.handler;

import com.cloudx.platform.websocket.converter.ConnectedMessageConverter;
import com.cloudx.platform.websocket.core.MessageHandler;
import com.cloudx.platform.websocket.model.message.ConnectedMessage;
import com.cloudx.platform.websocket.model.message.MessageType;
import com.cloudx.platform.websocket.core.SessionWrapper;

/**
 * 打开连接成功消息处理
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:31
 */
public class ConnectedMessageHandler implements MessageHandler<ConnectedMessage> {

    private final ConnectedMessageConverter connectedMessageConverter;

    public ConnectedMessageHandler(ConnectedMessageConverter connectedMessageConverter) {
        this.connectedMessageConverter = connectedMessageConverter;
    }

    @Override
    public String getSupportedMessageType() {
        return MessageType.CONNECTED.name();
    }

    @Override
    public void handleMessage(ConnectedMessage message, SessionWrapper sessionWrapper) {
        String serialize = connectedMessageConverter.serialize(message);
        sessionWrapper.send(serialize);
    }
}
