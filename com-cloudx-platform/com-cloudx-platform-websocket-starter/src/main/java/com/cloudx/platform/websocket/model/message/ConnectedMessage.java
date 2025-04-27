package com.cloudx.platform.websocket.model.message;

/**
 * 建立连接成功消息
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:32
 */
public class ConnectedMessage extends BaseMessage {

    public ConnectedMessage() {
        super(MessageType.CONNECTED);
    }
}
