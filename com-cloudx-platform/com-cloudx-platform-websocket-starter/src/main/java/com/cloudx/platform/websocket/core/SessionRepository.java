package com.cloudx.platform.websocket.core;

import java.util.Optional;

/**
 * Session 存储层
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:11
 */
public interface SessionRepository {

    void save(SessionWrapper sessionWrapper);

    Optional<SessionWrapper> getBySessionId(String sessionId);

    Optional<SessionWrapper> getByUserId(String userId);

    void remove(String sessionId);
}
