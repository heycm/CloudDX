package com.cloudx.platform.websocket.core.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serial;
import java.io.Serializable;

/**
 * Session包装类
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 15:33
 */
@Data
@NoArgsConstructor
public class SessionWrapper implements Serializable {

    @Serial
    private static final long serialVersionUID = -433781215946716308L;

    /**
     * sessionId
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 打开连接时间
     */
    private long connectedTime = System.currentTimeMillis();

    /**
     * 最后一次心跳时间
     */
    private long lastHeartbeatTime = System.currentTimeMillis();

    /**
     * 尝试ping心跳次数
     */
    private int pingAttempts;

    /**
     * 最后一次ping心跳时间
     */
    private long lastPingTime;

    public SessionWrapper(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public void updateHeartbeat() {
        this.lastHeartbeatTime = System.currentTimeMillis();
        this.pingAttempts = 0;
        this.lastPingTime = 0;
    }

    public void incrementPingAttempt() {
        this.pingAttempts++;
        this.lastPingTime = System.currentTimeMillis();
    }
}
