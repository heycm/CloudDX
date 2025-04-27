package com.cloudx.platform.websocket.handler;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.core.MessageHandler;
import com.cloudx.platform.websocket.core.MessageSender;
import com.cloudx.platform.websocket.model.message.ConnectedMessage;
import com.cloudx.platform.websocket.model.message.MessageType;
import com.cloudx.platform.websocket.model.session.SessionWrapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 打开连接成功消息处理
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:31
 */
public class ConnectedMessageHandler implements MessageHandler<ConnectedMessage> {

    @Override
    public MessageType getSupportedType() {
        return MessageType.CONNECTED;
    }

    @Override
    public void handleMessage(ConnectedMessage message, SessionWrapper sessionWrapper) {
        TextMessage textMessage = new TextMessage(Jackson.toJson(message));
        WebSocketSession session = sessionWrapper.getSession();
        MessageSender.send(session, textMessage);
    }
}
