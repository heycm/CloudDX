package com.cloudx.platform.websocket.service;

import com.cloudx.platform.websocket.model.session.AuthResult;
import org.springframework.http.server.ServerHttpRequest;

/**
 * 连接认证服务
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 20:45
 */
public interface AuthService {

    /**
     * 验证连接
     * @param request 请求
     * @return 认证结果
     */
    AuthResult validate(ServerHttpRequest request);

}
