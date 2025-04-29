package com.cloudx.platform.websocket.handler;

import com.cloudx.platform.websocket.converter.JsonMessageConverter;
import com.cloudx.platform.websocket.core.MessageHandler;
import com.cloudx.platform.websocket.model.message.JsonMessage;
import com.cloudx.platform.websocket.model.message.MessageType;
import com.cloudx.platform.websocket.core.SessionWrapper;

/**
 * JSON消息处理器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:56
 */
public class JsonMessageHandler implements MessageHandler<JsonMessage> {

    private final JsonMessageConverter jsonMessageConverter;

    public JsonMessageHandler(JsonMessageConverter jsonMessageConverter) {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    @Override
    public String getSupportedMessageType() {
        return MessageType.JSON.name();
    }

    @Override
    public void handleMessage(JsonMessage message, SessionWrapper sessionWrapper) {

    }
}
