package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.BaseMessage;

/**
 * 消息分发器：将消息转发到处理器执行处理逻辑
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:12
 */
public interface MessageDispatcher {

    /**
     * 分发消息
     * @param message   消息
     */
    void dispatch(BaseMessage message);
}
