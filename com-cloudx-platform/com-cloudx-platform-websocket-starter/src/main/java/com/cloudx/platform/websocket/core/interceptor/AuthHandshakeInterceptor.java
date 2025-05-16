package com.cloudx.platform.websocket.core.interceptor;

import com.cloudx.common.tools.UUIDUtil;
import com.cloudx.platform.websocket.core.auth.AuthResult;
import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;

import java.io.IOException;
import java.util.Map;

import com.cloudx.platform.websocket.core.session.SessionWrapper;
import org.springframework.http.HttpStatus;
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
            attributes.put("USER_ID", "guest");
            return true;
        }
        AuthResult result = webSocketAuthenticator.authenticate(request);
        if (result == null || !result.isSuccess()) {
            String errmsg = result == null ? "Authentication failed" : result.getErrMsg();
            sendError(response, errmsg);
            return false;
        }
        attributes.put("USER_ID", result.getUserId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // do nothing.
    }

    private void sendError(ServerHttpResponse response, String message) {
        try {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getBody().write(message.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to send authentication error", e);
        }
    }
}
