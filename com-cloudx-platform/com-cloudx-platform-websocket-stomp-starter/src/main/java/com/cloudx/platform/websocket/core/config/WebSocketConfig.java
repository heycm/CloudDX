package com.cloudx.platform.websocket.core.config;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import com.cloudx.platform.websocket.interceptor.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

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

    public WebSocketConfig(WebSocketProperties webSocketProperties, WebSocketAuthenticator webSocketAuthenticator) {
        this.webSocketProperties = webSocketProperties;
        this.webSocketAuthenticator = webSocketAuthenticator;
    }

    @Bean
    public HandshakeInterceptor authHandshakeInterceptor() {
        return new AuthHandshakeInterceptor(webSocketAuthenticator, webSocketProperties.isAuthEnabled());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketProperties.getEndpoint())
                .addInterceptors(authHandshakeInterceptor())
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(webSocketProperties.getDestinationPrefixe())
                .enableStompBrokerRelay(webSocketProperties.getSimpleBroker())
                .setRelayHost(webSocketProperties.getRedis().getHost())
                .setRelayPort(webSocketProperties.getRedis().getPort())
                .setSystemPasscode(webSocketProperties.getRedis().getPassword());
    }
}
