package com.cloudx.platform.websocket.repository;

import com.cloudx.platform.websocket.core.session.SessionWrapper;

/**
 * Session存储
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 16:57
 */
public interface SessionRepository {

    /**
     * 保存会话
     * @param session 会话
     */
    void save(SessionWrapper session);

    /**
     * 获取会话
     * @param sessionId 会话ID
     * @return 会话
     */
    SessionWrapper getSession(String sessionId);

    /**
     * 获取用户会话
     * @param userId 用户ID
     * @return 会话
     */
    SessionWrapper getUser(String userId);

    /**
     * 删除会话
     * @param sessionId 会话ID
     */
    void removeSession(String sessionId);

    /**
     * 删除用户会话
     * @param userId 用户ID
     */
    void removeUser(String userId);
}
