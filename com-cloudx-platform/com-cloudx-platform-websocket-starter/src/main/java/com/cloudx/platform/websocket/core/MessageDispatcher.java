package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.BaseMessage;

/**
 * 消息分发器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:12
 */
public interface MessageDispatcher {

    void dispatch(BaseMessage message, String sessionId);

    void broadcast(BaseMessage message, String sessionId);
}
