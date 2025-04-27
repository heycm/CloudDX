package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.BaseMessage;
import com.cloudx.platform.websocket.model.message.MessageType;
import com.cloudx.platform.websocket.model.session.SessionWrapper;
import java.io.IOException;

/**
 * 消息处理器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:13
 */
public interface MessageHandler<T extends BaseMessage> {

    /**
     * 支持的消息类型
     * @return MessageType
     */
    MessageType getSupportedType();

    /**
     * 消息处理逻辑
     * @param message        消息
     * @param sessionWrapper session
     */
    void handleMessage(T message, SessionWrapper sessionWrapper);
}
