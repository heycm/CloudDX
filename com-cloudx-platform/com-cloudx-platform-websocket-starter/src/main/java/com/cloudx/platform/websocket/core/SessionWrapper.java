package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.exception.SendMessageException;
import com.cloudx.platform.websocket.exception.SessionException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.SessionLimitExceededException;

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

    /**
     * Session
     */
    @JsonIgnore
    private WebSocketSession session;

    /**
     * 打开连接时间
     */
    private long connectedTime;

    /**
     * 最近一次心跳时间
     */
    private long latestHeartbeatTime;

    /**
     * 心跳重试次数
     */
    private int retryHeartbeatCount;

    /**
     * Session是否连接、有效、存活
     * @return
     */
    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    /**
     * 发送消息
     * @param message 消息内容
     */
    public void send(String message) {
        if (session == null) {
            throw new SessionException("Session is null");
        }
        if (!session.isOpen()) {
            throw new SessionException("Connection is closed");
        }
        try {
            session.sendMessage(new TextMessage(message));
        } catch (SessionLimitExceededException e) {
            throw e;
        } catch (IOException e) {
            throw new SendMessageException("Connection send message error", e);
        }
    }
}
