package com.cloudx.platform.websocket.model.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * Session 包装类
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:17
 */
@Data
public class SessionWrapper {

    private String sessionId;

    @JsonIgnore
    private WebSocketSession session;

    public boolean isConnected() {
        return session != null && session.isOpen();
    }
}
