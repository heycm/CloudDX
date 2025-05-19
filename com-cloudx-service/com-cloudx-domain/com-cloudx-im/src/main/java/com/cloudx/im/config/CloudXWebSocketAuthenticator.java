package com.cloudx.im.config;

import com.cloudx.platform.websocket.core.auth.AuthResult;
import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/5/16 11:42
 */
public class CloudXWebSocketAuthenticator implements WebSocketAuthenticator {
    @Override
    public AuthResult authenticate(ServerHttpRequest request) {
        // return new AuthResult().setSuccess(false).setErrMsg("认证失败");
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Token");
        System.out.println("token:" + token);
        return new AuthResult().setSuccess(true).setUserId("123456");
    }
}
