package com.cloudx.platform.websocket.core.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Session 会话存储
 * @author heycm
 * @version 1.0
 * @since 2025/5/7 11:09
 */
public class WebSocketSessionLocalStorage {

    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    public static WebSocketSession get(String sessionId) {
        return SESSIONS.get(sessionId);
    }

    public static void put(WebSocketSession session) {
        SESSIONS.put(session.getId(), session);
    }

    public static void remove(String sessionId) {
        SESSIONS.remove(sessionId);
    }

    public static boolean close(String sessionId) {
        WebSocketSession session = get(sessionId);
        if (session != null) {
            remove(sessionId);
            if (session.isOpen()) {
                try {
                    session.close();
                } catch (Exception ignored) {

                }
            }
            return true;
        }
        return false;
    }
}
