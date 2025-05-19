package com.cloudx.platform.websocket.repository.impl;

import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.repository.GroupRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 以Redis作为群组缓存实现
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:15
 */
public class RedisGroupRepository implements GroupRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisGroupRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void join(String groupId, String sessionId) {
        String groupKey = ServerConstant.GROUP_KEY_PREFIX + groupId;
        redisTemplate.boundSetOps(groupKey).add(sessionId);
        String joinKey = ServerConstant.GROUP_JOIN_PREFIX + sessionId;
        redisTemplate.boundSetOps(joinKey).add(groupId);
    }

    @Override
    public void leave(String groupId, String sessionId) {
        String groupKey = ServerConstant.GROUP_KEY_PREFIX + groupId;
        Long removed = redisTemplate.boundSetOps(groupKey).remove(sessionId);
        if (removed != null && removed > 0 && this.getGroupMemberSize(groupId) == 0) {
            redisTemplate.delete(groupKey);
        }
        String joinKey = ServerConstant.GROUP_JOIN_PREFIX + sessionId;
        removed = redisTemplate.boundSetOps(joinKey).remove(groupId);
        if (removed != null && removed > 0 && this.getJoinSize(groupId) == 0) {
            redisTemplate.delete(joinKey);
        }
    }

    @Override
    public long getGroupMemberSize(String groupId) {
        Long size = redisTemplate.boundSetOps(ServerConstant.GROUP_KEY_PREFIX + groupId).size();
        return Optional.ofNullable(size).map(Long::intValue).orElse(0);
    }

    @Override
    public long getJoinSize(String sessionId) {
        Long size = redisTemplate.boundSetOps(ServerConstant.GROUP_JOIN_PREFIX + sessionId).size();
        return Optional.ofNullable(size).map(Long::intValue).orElse(0);
    }

    @Override
    public void leaveAll(String sessionId) {
        // 获取该通道加入的群组集合
        String joinKey = ServerConstant.GROUP_JOIN_PREFIX + sessionId;
        Set<Object> members = redisTemplate.boundSetOps(joinKey).members();
        if (!CollectionUtils.isEmpty(members)) {
            for (Object groupId : members) {
                // 依次退出群组，如果是最后一个成员，则解散群组
                String groupKey = ServerConstant.GROUP_KEY_PREFIX + groupId;
                Long remove = redisTemplate.boundSetOps(groupKey).remove(sessionId);
                if (remove != null && remove > 0 && this.getGroupMemberSize(groupId.toString()) == 0) {
                    redisTemplate.delete(groupKey);
                }
            }
            // 删除该通道加入的群组集合
            redisTemplate.delete(joinKey);
        }
    }

    @Override
    public Set<String> getGroupMembers(String groupId) {
        String groupKey = ServerConstant.GROUP_KEY_PREFIX + groupId;
        Set<Object> members = redisTemplate.boundSetOps(groupKey).members();
        return CollectionUtils.isEmpty(members) ? Collections.emptySet()
                : members.stream().map(Object::toString).collect(Collectors.toSet());
    }
}
