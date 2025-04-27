package com.cloudx.platform.websocket.core;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.exception.SendMessageException;
import com.cloudx.platform.websocket.exception.SessionException;
import com.cloudx.platform.websocket.model.message.BaseMessage;
import java.io.IOException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 消息发送器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 22:06
 */
public class MessageSender {

    private MessageSender() {
    }

    public static void send(WebSocketSession session, BaseMessage message) {
        MessageSender.send(session, Jackson.toJson(message));
    }

    public static void send(WebSocketSession session, String message) {
        MessageSender.send(session, new TextMessage(message));
    }

    public static void send(WebSocketSession session, WebSocketMessage<?> message) {
        if (session == null) {
            throw new SessionException("Session is null");
        }
        if (!session.isOpen()) {
            throw new SessionException("Connection is closed");
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new SendMessageException("Connection send message error", e);
        }
    }

}
