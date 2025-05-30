package com.cloudx.platform.websocket.core.decorator;

import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import com.cloudx.platform.websocket.core.session.WebSocketSessionLocalStorage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * 连接处理
 * @author heycm
 * @version 1.0
 * @since 2025/5/7 11:00
 */
public class ConnectionDecorator extends WebSocketHandlerDecorator {

    private final SessionRepository sessionRepository;

    public ConnectionDecorator(WebSocketHandler delegate, SessionRepository sessionRepository) {
        super(delegate);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String userId = (String) session.getAttributes().get(ServerConstant.ATTRIBUTE_USER_KEY);
        SessionWrapper sessionWrapper = new SessionWrapper(sessionId, userId);
        sessionRepository.save(sessionWrapper);
        WebSocketSessionLocalStorage.put(userId, session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = (String) session.getAttributes().get(ServerConstant.ATTRIBUTE_USER_KEY);
        WebSocketSessionLocalStorage.remove(userId);
        sessionRepository.remove(userId);
        super.afterConnectionClosed(session, closeStatus);
    }
}
