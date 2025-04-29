package com.cloudx.platform.websocket.dispatcher;

import com.cloudx.platform.websocket.core.MessageDispatcher;
import com.cloudx.platform.websocket.core.MessageHandler;
import com.cloudx.platform.websocket.core.MessageHandlerRegistry;
import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.model.message.BaseMessage;
import com.cloudx.platform.websocket.core.SessionWrapper;
import java.util.Optional;

/**
 * 本地消息分发
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:46
 */
public class LocalMessageDispatcher implements MessageDispatcher {

    private final SessionRepository sessionRepository;
    private final MessageHandlerRegistry messageHandlerRegistry;

    public LocalMessageDispatcher(SessionRepository sessionRepository, MessageHandlerRegistry messageHandlerRegistry) {
        this.sessionRepository = sessionRepository;
        this.messageHandlerRegistry = messageHandlerRegistry;
    }

    @Override
    public void dispatch(BaseMessage message) {
        Optional<SessionWrapper> sessionWrapperOptional = sessionRepository.getBySessionId(message.getCurrentSessionId());
        if (!sessionWrapperOptional.isPresent()) {
            return;
        }
        Optional<MessageHandler<BaseMessage>> handlerOptional = messageHandlerRegistry.getHandler(message.getMessageType());
        if (!handlerOptional.isPresent()) {
            return;
        }
        handlerOptional.get().handleMessage(message, sessionWrapperOptional.get());
    }
}
