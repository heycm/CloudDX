package com.cloudx.platform.websocket.model.message;

import lombok.Data;

/**
 * JSON消息
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:38
 */
@Data
public class JsonMessage extends BaseMessage {

    /**
     * JSON数据
     */
    private String data;

    public JsonMessage() {
        setMessageType(MessageType.JSON.name());
    }
}
