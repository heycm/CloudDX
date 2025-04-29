package com.cloudx.platform.websocket.repository;

import com.cloudx.platform.websocket.core.SessionRepository;
import com.cloudx.platform.websocket.core.SessionWrapper;

import java.util.Optional;
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
    private final ConcurrentMap<String, SessionWrapper> users = new ConcurrentHashMap<>();

    @Override
    public void save(SessionWrapper sessionWrapper) {
        sessions.put(sessionWrapper.getSessionId(), sessionWrapper);
        if (sessionWrapper.getUserId() != null) {
            users.put(sessionWrapper.getUserId(), sessionWrapper);
        }
    }

    @Override
    public Optional<SessionWrapper> getBySessionId(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public Optional<SessionWrapper> getByUserId(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void remove(String sessionId) {
        getBySessionId(sessionId).ifPresent(sessionWrapper -> {
            sessions.remove(sessionWrapper.getSessionId());
            if (sessionWrapper.getUserId() != null) {
                users.remove(sessionWrapper.getUserId());
            }
        });
    }
}
