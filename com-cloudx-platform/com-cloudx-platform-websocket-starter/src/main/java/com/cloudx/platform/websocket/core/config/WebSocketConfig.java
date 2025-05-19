package com.cloudx.platform.websocket.core.config;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import com.cloudx.platform.websocket.core.decorator.CompressionDecorator;
import com.cloudx.platform.websocket.core.decorator.ConnectionDecorator;
import com.cloudx.platform.websocket.core.handshake.UserHandshakeHandler;
import com.cloudx.platform.websocket.core.interceptor.AuthHandshakeInterceptor;
import com.cloudx.platform.websocket.repository.SessionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocket 配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 21:59
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketProperties webSocketProperties;

    private final WebSocketAuthenticator webSocketAuthenticator;

    private final SessionRepository sessionRepository;

    public WebSocketConfig(WebSocketProperties webSocketProperties, WebSocketAuthenticator webSocketAuthenticator, SessionRepository sessionRepository) {
        this.webSocketProperties = webSocketProperties;
        this.webSocketAuthenticator = webSocketAuthenticator;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // registry.addEndpoint(webSocketProperties.getEndpoint())
        //         .addInterceptors(new AuthHandshakeInterceptor(webSocketAuthenticator, webSocketProperties.isAuthEnabled()))
        //         .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
        //         .withSockJS()
        //         .setHeartbeatTime(webSocketProperties.getHeartbeat().getCheckInterval());

        registry.addEndpoint(webSocketProperties.getEndpoint())
                .setHandshakeHandler(new UserHandshakeHandler())
                .addInterceptors(new AuthHandshakeInterceptor(webSocketAuthenticator, webSocketProperties.isAuthEnabled()));
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(webSocketProperties.getAppDestPrefixs())
                .setUserDestinationPrefix(webSocketProperties.getUserDestPrefix());
        registry.enableSimpleBroker(webSocketProperties.getClientDestPrefixs());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setSendTimeLimit(webSocketProperties.getSendTimeLimit())
                .setSendBufferSizeLimit(webSocketProperties.getSendBufferSizeLimit())
                .addDecoratorFactory(session -> {
                    WebSocketHandlerDecorator delegate = new CompressionDecorator(session);
                    delegate = new ConnectionDecorator(delegate, sessionRepository);
                    return delegate;
                });
    }
}
