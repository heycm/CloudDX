package com.cloudx.platform.websocket.interceptor;

import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 握手认证拦截器
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 21:47
 */
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final WebSocketAuthenticator webSocketAuthenticator;

    private final boolean authEnabled;

    public AuthHandshakeInterceptor(WebSocketAuthenticator webSocketAuthenticator, boolean authEnabled) {
        this.webSocketAuthenticator = webSocketAuthenticator;
        this.authEnabled = authEnabled;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        if (!authEnabled) {
            return true;
        }
        return webSocketAuthenticator.authenticate(request, response);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // do nothing.
    }
}
