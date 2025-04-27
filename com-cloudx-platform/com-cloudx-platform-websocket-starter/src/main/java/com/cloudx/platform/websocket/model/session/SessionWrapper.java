package com.cloudx.platform.websocket.model.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * Session 包装类
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:17
 */
@Data
@Slf4j
public class SessionWrapper {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private String userId;

    @JsonIgnore
    private WebSocketSession session;

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    public void close() {
        if (this.isConnected()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭连接异常", e);
            }
        }
    }
}
