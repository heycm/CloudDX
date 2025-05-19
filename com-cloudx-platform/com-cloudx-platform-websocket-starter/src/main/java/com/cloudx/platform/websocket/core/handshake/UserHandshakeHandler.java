package com.cloudx.platform.websocket.core.handshake;

import com.cloudx.platform.websocket.constant.ServerConstant;
import java.security.Principal;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * 握手处理设置会话用户
 * @author heycm
 * @version 1.0
 * @since 2025/5/19 22:06
 */
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String userId = (String) attributes.get(ServerConstant.ATTRIBUTE_USER_KEY);
        return () -> userId;
    }
}
