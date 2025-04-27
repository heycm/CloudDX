package com.cloudx.platform.websocket.endpoint;

import com.cloudx.platform.websocket.core.MessageDispatcher;
import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.model.session.SessionWrapper;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.standard.SpringConfigurator;

/**
 * 服务端点
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:14
 */
@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
public class WebSocketEndpoint {

    private final SessionRepository sessionRepository;
    private final MessageDispatcher messageDispatcher;

    public WebSocketEndpoint(SessionRepository sessionRepository, MessageDispatcher messageDispatcher) {
        this.sessionRepository = sessionRepository;
        this.messageDispatcher = messageDispatcher;
    }

    @OnOpen
    public void onOpen(WebSocketSession session, EndpointConfig config) {
        // auth
        SessionWrapper sessionWrapper = new SessionWrapper();
        sessionWrapper.setSession(session);
        sessionRepository.save(sessionWrapper);
    }

    @OnMessage
    public void onMessage(WebSocketSession session, String message) {

    }
}
