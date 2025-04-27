package com.cloudx.platform.websocket.repository;

import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.model.session.SessionWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Session 内存存储
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 18:09
 */
public class InMemorySessionRepository implements SessionRepository {

    private final ConcurrentMap<String, SessionWrapper> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(SessionWrapper sessionWrapper) {
        sessions.put(sessionWrapper.getSessionId(), sessionWrapper);
    }

    @Override
    public SessionWrapper get(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public void remove(SessionWrapper sessionWrapper) {
        sessions.remove(sessionWrapper.getSessionId());
    }
}
