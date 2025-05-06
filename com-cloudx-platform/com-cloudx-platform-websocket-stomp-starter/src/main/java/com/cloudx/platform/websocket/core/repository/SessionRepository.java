package com.cloudx.platform.websocket.core.repository;

import com.cloudx.platform.websocket.core.session.SessionWrapper;

/**
 * Session存储
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 16:57
 */
public interface SessionRepository {

    void save(SessionWrapper session);

    SessionWrapper getChannel(String channelId);

    SessionWrapper getUser(String userId);

    void removeChannel(String channelId);

    void removeUser(String userId);
}
