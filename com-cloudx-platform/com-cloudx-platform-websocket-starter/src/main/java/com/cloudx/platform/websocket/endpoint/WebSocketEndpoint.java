package com.cloudx.platform.websocket.endpoint;

import com.cloudx.platform.websocket.core.MessageDispatcher;
import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.exception.SessionException;
import com.cloudx.platform.websocket.model.message.ConnectedMessage;
import com.cloudx.platform.websocket.model.session.SessionWrapper;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.standard.SpringConfigurator;

/**
 * 服务端点
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:14
 */
@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
@Slf4j
public class WebSocketEndpoint {

    private final SessionRepository sessionRepository;
    private final MessageDispatcher messageDispatcher;

    public WebSocketEndpoint(SessionRepository sessionRepository, MessageDispatcher messageDispatcher) {
        this.sessionRepository = sessionRepository;
        this.messageDispatcher = messageDispatcher;
    }

    @OnOpen
    public void onOpen(WebSocketSession session, EndpointConfig config) {
        Object userId = config.getUserProperties().get("userId");
        SessionWrapper sessionWrapper = new SessionWrapper();
        sessionWrapper.setSessionId(session.getId());
        sessionWrapper.setUserId(userId.toString());
        sessionWrapper.setSession(session);
        sessionRepository.save(sessionWrapper);

        ConnectedMessage message = new ConnectedMessage();
        messageDispatcher.dispatch(message, sessionWrapper.getSessionId());
    }

    @OnMessage
    public void onMessage(WebSocketSession session, String message) {

    }

    @OnClose
    public void onClose(WebSocketSession session, CloseReason closeReason) {
        sessionRepository.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket Error: {}", throwable.getMessage(), throwable);
        if (throwable instanceof SessionException) {
            sessionRepository.remove(session.getId());
        }
    }
}
