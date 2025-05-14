package com.cloudx.platform.websocket.repository;

import java.util.Set;

/**
 * 群组存储层
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:14
 */
public interface GroupRepository {

    /**
     * 加入群组
     * @param groupId   群组ID
     * @param sessionId 会话ID
     */
    void join(String groupId, String sessionId);

    /**
     * 退出群组
     * @param groupId   群组ID
     * @param sessionId 会话ID
     */
    void leave(String groupId, String sessionId);

    /**
     * 获取群组成员数量
     * @param groupId 群组ID
     * @return
     */
    long getGroupMemberCount(String groupId);

    /**
     * 获取会话加入群组的数量
     * @param sessionId 会话ID
     * @return
     */
    long getSessionGroupCount(String sessionId);

    /**
     * 退出所有群组
     * @param sessionId 会话ID
     */
    void leaveAll(String sessionId);

    /**
     * 获取群组成员
     * @param groupId 群组ID
     * @return
     */
    Set<String> getGroupMembers(String groupId);
}
