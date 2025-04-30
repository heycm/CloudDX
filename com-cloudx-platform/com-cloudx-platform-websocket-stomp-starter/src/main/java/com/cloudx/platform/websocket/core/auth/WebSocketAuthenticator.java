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
     * @param response 响应
     * @return 认证结果：返回 false 则拒绝连接，使用 response 设置响应码和错误信息
     */
    boolean authenticate(ServerHttpRequest request, ServerHttpResponse response);
}
