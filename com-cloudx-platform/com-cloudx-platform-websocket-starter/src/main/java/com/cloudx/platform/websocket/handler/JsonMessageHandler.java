package com.cloudx.platform.websocket.handler;

import com.cloudx.platform.websocket.core.MessageHandler;
import com.cloudx.platform.websocket.model.message.JsonMessage;
import com.cloudx.platform.websocket.model.message.MessageType;
import com.cloudx.platform.websocket.model.session.SessionWrapper;

/**
 * JSON消息处理器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:56
 */
public class JsonMessageHandler implements MessageHandler<JsonMessage> {

    @Override
    public MessageType getSupportedType() {
        return MessageType.JSON;
    }

    @Override
    public void handleMessage(JsonMessage message, SessionWrapper sessionWrapper) {

    }
}
