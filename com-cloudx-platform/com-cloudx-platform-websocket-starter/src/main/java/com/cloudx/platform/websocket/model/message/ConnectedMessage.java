package com.cloudx.platform.websocket.model.message;

import lombok.Data;

/**
 * 建立连接成功消息
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:32
 */
@Data
public class ConnectedMessage extends BaseMessage {

    private String content = "Connect successful";

    public ConnectedMessage() {
        setMessageType(MessageType.CONNECTED.name());
    }
}
