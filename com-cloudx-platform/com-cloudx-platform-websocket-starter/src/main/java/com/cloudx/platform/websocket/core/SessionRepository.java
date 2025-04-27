package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.session.SessionWrapper;

/**
 * Session 存储层
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:11
 */
public interface SessionRepository {

    void save(SessionWrapper sessionWrapper);

    SessionWrapper get(String sessionId);

    void remove(String sessionId);

    void remove(SessionWrapper sessionWrapper);
}
