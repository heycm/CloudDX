package com.cloudx.platform.websocket.core.repository.impl;

import com.cloudx.platform.websocket.core.repository.GroupRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

/**
 * 以Redis作为群组缓存实现
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:15
 */
public class RedisGroupRepository implements GroupRepository {

    /**
     * 群组KEY前缀
     */
    private static final String GROUP_KEY_PREFIX = "websocket:group:";

    /**
     * 通道加入群组KEY前缀，记录通道加入的群组ID
     */
    private static final String CHANNEL_JOIN_KEY_PREFIX = "websocket:group:channel:join:";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisGroupRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void join(String groupId, String channelId) {
        String groupKey = GROUP_KEY_PREFIX + groupId;
        redisTemplate.boundSetOps(groupKey).add(channelId);
        String joinKey = CHANNEL_JOIN_KEY_PREFIX + channelId;
        redisTemplate.boundSetOps(joinKey).add(groupId);
    }

    @Override
    public void leave(String groupId, String channelId) {
        String groupKey = GROUP_KEY_PREFIX + groupId;
        Long removed = redisTemplate.boundSetOps(groupKey).remove(channelId);
        if (removed != null && removed > 0 && this.getGroupMemberCount(groupId) == 0) {
            redisTemplate.delete(groupKey);
        }
        String joinKey = CHANNEL_JOIN_KEY_PREFIX + channelId;
        removed = redisTemplate.boundSetOps(joinKey).remove(groupId);
        if (removed != null && removed > 0 && this.getChannelGroupCount(groupId) == 0) {
            redisTemplate.delete(joinKey);
        }
    }

    @Override
    public long getGroupMemberCount(String groupId) {
        Long size = redisTemplate.boundSetOps(GROUP_KEY_PREFIX + groupId).size();
        return Optional.ofNullable(size).map(Long::intValue).orElse(0);
    }

    @Override
    public long getChannelGroupCount(String channelId) {
        Long size = redisTemplate.boundSetOps(CHANNEL_JOIN_KEY_PREFIX + channelId).size();
        return Optional.ofNullable(size).map(Long::intValue).orElse(0);
    }

    @Override
    public void leaveAll(String channelId) {
        // 获取该通道加入的群组集合
        String joinKey = CHANNEL_JOIN_KEY_PREFIX + channelId;
        Set<Object> members = redisTemplate.boundSetOps(joinKey).members();
        if (!CollectionUtils.isEmpty(members)) {
            for (Object groupId : members) {
                // 依次退出群组，如果是最后一个成员，则解散群组
                String groupKey = GROUP_KEY_PREFIX + groupId;
                Long remove = redisTemplate.boundSetOps(groupKey).remove(channelId);
                if (remove != null && remove > 0 && this.getGroupMemberCount(groupId.toString()) == 0) {
                    redisTemplate.delete(groupKey);
                }
            }
            // 删除该通道加入的群组集合
            redisTemplate.delete(joinKey);
        }
    }
}
