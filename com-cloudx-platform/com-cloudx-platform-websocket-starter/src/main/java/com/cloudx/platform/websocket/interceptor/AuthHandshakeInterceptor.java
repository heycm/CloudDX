package com.cloudx.platform.websocket.interceptor;

import com.cloudx.platform.websocket.model.auth.AuthResult;
import com.cloudx.platform.websocket.service.AuthService;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * 握手认证拦截器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 20:41
 */
public class AuthHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final AuthService authService;

    public AuthHandshakeInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        AuthResult result = authService.validate(request);
        if (!result.isSuccess()) {
            return false;
        }
        String userId = result.getUserId();
        attributes.put("userId", userId);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
