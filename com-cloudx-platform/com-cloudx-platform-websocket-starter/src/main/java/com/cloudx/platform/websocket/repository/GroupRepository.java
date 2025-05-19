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
     * @param groupId 群组ID
     * @param user    用户
     */
    void join(String groupId, String user);

    /**
     * 退出群组
     * @param groupId 群组ID
     * @param user    用户
     */
    void leave(String groupId, String user);

    /**
     * 退出所有群组
     * @param user 用户
     */
    void leaveAll(String user);

    /**
     * 获取群组成员数量
     * @param groupId 群组
     * @return
     */
    long getGroupMemberSize(String groupId);

    /**
     * 获取用户加入群组的数量
     * @param user 用户
     * @return
     */
    long getJoinSize(String user);

    /**
     * 获取群组成员
     * @param groupId 群组ID
     * @return
     */
    Set<String> getGroupMembers(String groupId);
}
