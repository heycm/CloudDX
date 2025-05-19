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

    public static WebSocketSession get(String user) {
        return SESSIONS.get(user);
    }

    public static void put(String user, WebSocketSession session) {
        SESSIONS.put(user, session);
    }

    public static void remove(String user) {
        SESSIONS.remove(user);
    }

    public static boolean close(String user) {
        WebSocketSession session = get(user);
        if (session != null) {
            remove(user);
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

    public static boolean exists(String user) {
        return SESSIONS.containsKey(user);
    }
}
