package com.cloudx.platform.websocket.core.auth;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * WebSocket连接认证：在 WebSocket 握手阶段（HTTP 升级为 WS 协议前）进行认证
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 21:47
 */
public interface WebSocketAuthenticator {

    /**
     * 连接认证
     * @param request  请求
     * @return 认证结果
     */
    AuthResult authenticate(ServerHttpRequest request);
}
