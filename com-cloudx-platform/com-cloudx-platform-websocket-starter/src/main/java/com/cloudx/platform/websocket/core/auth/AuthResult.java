package com.cloudx.platform.websocket.core.auth;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 认证结果
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 18:14
 */
@Data
@Accessors(chain = true)
public class AuthResult {

    private boolean success;

    private String userId;

    private String errMsg;
}
