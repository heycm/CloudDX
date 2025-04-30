package com.cloudx.platform.websocket.routing;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 群组注册
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 22:45
 */
public class ChannelGroupRegistry {

    private static final String CHANNEL_GROUP_KEY_PREFIX = "websocket:channel:group:";

    private final RedisTemplate<String, Object> redisTemplate;

    public ChannelGroupRegistry(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加入群组
     * @param groupId   群组ID
     * @param channelId 通道ID
     */
    public void join(String groupId, String channelId) {
        redisTemplate.opsForSet().add(CHANNEL_GROUP_KEY_PREFIX + groupId, channelId);
    }

    /**
     * 退出群组
     * @param groupId   群组ID
     * @param channelId 通道ID
     */
    public void leave(String groupId, String channelId) {
        Long remove = redisTemplate.opsForSet().remove(CHANNEL_GROUP_KEY_PREFIX + groupId, channelId);
        if (remove != null && remove > 0 && this.getMemberCount(groupId) == 0) {
            this.dismiss(groupId);
        }
    }

    /**
     * 获取群组成员
     * @param groupId 群组ID
     * @return 群组成员
     */
    public Set<String> getMembers(String groupId) {
        Set<Object> members = redisTemplate.opsForSet().members(CHANNEL_GROUP_KEY_PREFIX + groupId);
        return members.stream().map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * 判断是否是群组成员
     * @param groupId   群组ID
     * @param channelId 通道ID
     * @return true-是群组成员，false-不是群组成员
     */
    public boolean isMember(String groupId, String channelId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(CHANNEL_GROUP_KEY_PREFIX + groupId, channelId));
    }

    /**
     * 获取群组成员数量
     * @param groupId 群组ID
     * @return 群组成员数量
     */
    public int getMemberCount(String groupId) {
        return redisTemplate.opsForSet().size(CHANNEL_GROUP_KEY_PREFIX + groupId).intValue();
    }

    /**
     * 解散群组
     * @param groupId 群组ID
     */
    public void dismiss(String groupId) {
        redisTemplate.delete(CHANNEL_GROUP_KEY_PREFIX + groupId);
    }

    /**
     * 清空所有群组中的通道
     * @param channelId 通道ID
     */
    public void clearChannel(String channelId) {
        redisTemplate.keys(CHANNEL_GROUP_KEY_PREFIX + "*").forEach(key -> {
            this.leave(key.substring(CHANNEL_GROUP_KEY_PREFIX.length()), channelId);
        });
    }
}
