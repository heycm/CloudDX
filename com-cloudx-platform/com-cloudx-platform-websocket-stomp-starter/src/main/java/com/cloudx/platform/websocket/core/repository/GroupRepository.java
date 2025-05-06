package com.cloudx.platform.websocket.core.repository;

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
     * @param channelId 通道ID
     */
    void join(String groupId, String channelId);

    /**
     * 退出群组
     * @param groupId   群组ID
     * @param channelId 通道ID
     */
    void leave(String groupId, String channelId);

    /**
     * 获取群组成员数量
     * @param groupId 群组ID
     * @return
     */
    long getGroupMemberCount(String groupId);

    /**
     * 获取频道加入群组的数量
     * @param channelId 通道ID
     * @return
     */
    long getChannelGroupCount(String channelId);

    /**
     * 退出所有群组
     * @param channelId 通道ID
     */
    void leaveAll(String channelId);
}
