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
     * @param user 用户
     * @return 会话
     */
    SessionWrapper get(String user);

    /**
     * 删除会话
     * @param user 用户
     */
    void remove(String user);
}
