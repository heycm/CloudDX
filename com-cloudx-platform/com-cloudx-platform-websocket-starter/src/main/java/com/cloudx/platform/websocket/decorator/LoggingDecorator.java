package com.cloudx.platform.websocket.decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * 消息日志装饰器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:06
 */
@Slf4j
public class LoggingDecorator extends WebSocketHandlerDecorator {

    public LoggingDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("WebSocket Connection {} opened", session.getId());
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("WebSocket Connection {} closed with status {}", session.getId(), closeStatus);
        }
        super.afterConnectionClosed(session, closeStatus);
    }
}
