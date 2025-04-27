package com.cloudx.platform.websocket.model.session;

import lombok.Data;

/**
 * Session 认证
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 20:48
 */
@Data
public class AuthResult {

    /**
     * 认证结果
     */
    private boolean success;

    /**
     * 用户ID
     */
    private String userId;
}
