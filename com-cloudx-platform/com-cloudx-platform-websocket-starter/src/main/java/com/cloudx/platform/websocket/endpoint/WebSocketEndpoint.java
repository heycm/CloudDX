package com.cloudx.platform.websocket.endpoint;

import com.cloudx.platform.websocket.core.MessageConverter;
import com.cloudx.platform.websocket.core.MessageConverterRegistry;
import com.cloudx.platform.websocket.core.MessageDispatcher;
import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.exception.SessionException;
import com.cloudx.platform.websocket.model.message.BaseMessage;
import com.cloudx.platform.websocket.model.message.ConnectedMessage;
import com.cloudx.platform.websocket.core.SessionWrapper;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.SessionLimitExceededException;
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
    private final MessageConverterRegistry messageConverterRegistry;

    public WebSocketEndpoint(SessionRepository sessionRepository, MessageDispatcher messageDispatcher, MessageConverterRegistry messageConverterRegistry) {
        this.sessionRepository = sessionRepository;
        this.messageDispatcher = messageDispatcher;
        this.messageConverterRegistry = messageConverterRegistry;
    }

    @OnOpen
    public void onOpen(WebSocketSession session, EndpointConfig config) {
        Object userId = config.getUserProperties().get("userId");
        SessionWrapper sessionWrapper = new SessionWrapper();
        sessionWrapper.setSessionId(session.getId());
        sessionWrapper.setUserId(userId.toString());
        sessionWrapper.setSession(session);
        sessionRepository.save(sessionWrapper);

        if (log.isDebugEnabled()) {
            log.debug("WebSocket connection {} opened, userId: {}", session.getId(), userId);
        }

        ConnectedMessage message = new ConnectedMessage();
        message.setCurrentSessionId(session.getId());
        messageDispatcher.dispatch(message);
    }

    @OnMessage
    public void onMessage(WebSocketSession session, String message) {
        MessageConverter<BaseMessage> converter = messageConverterRegistry.getConverter(message);
        BaseMessage deserialize = converter.deserialize(message);
        deserialize.setCurrentSessionId(session.getId());
        messageDispatcher.dispatch(deserialize);
    }

    @OnClose
    public void onClose(WebSocketSession session, CloseReason closeReason) {
        if (log.isDebugEnabled()) {
            log.debug("WebSocket connection {} closed with reason: {}", session.getId(), closeReason);
        }
        sessionRepository.remove(session.getId());
    }

    @OnError
    public void onError(WebSocketSession session, Throwable throwable) {
        log.error("WebSocket connection {} on error: {}", session.getId(), throwable.getMessage(), throwable);
        if (throwable instanceof SessionException) {
            sessionRepository.remove(session.getId());
        }
        // 并发超出限制，则主动关闭连接
        else if (throwable instanceof SessionLimitExceededException) {
            if (session.isOpen()) {
                try {
                    session.close();
                } catch (Exception e) {
                    log.error("WebSocket connection {} close error: {}", session.getId(), e.getMessage(), e);
                } finally {
                    sessionRepository.remove(session.getId());
                }
            }
        }
    }
}
