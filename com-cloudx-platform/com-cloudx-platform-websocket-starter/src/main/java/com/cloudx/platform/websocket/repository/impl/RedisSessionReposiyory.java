package com.cloudx.platform.websocket.repository.impl;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.repository.GroupRepository;
import com.cloudx.platform.websocket.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 以Redis作为Session存储层实现
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 16:57
 */
public class RedisSessionReposiyory implements SessionRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private final GroupRepository groupRepository;

    public RedisSessionReposiyory(RedisTemplate<String, Object> redisTemplate, GroupRepository groupRepository) {
        this.redisTemplate = redisTemplate;
        this.groupRepository = groupRepository;
    }

    @Override
    public void save(SessionWrapper session) {
        String sessionKey = ServerConstant.SESSION_KEY_PREFIX + session.getUserId();
        Map m = Jackson.toObject(Jackson.toJson(session), Map.class);
        redisTemplate.boundHashOps(sessionKey).putAll(m);
    }

    @Override
    public SessionWrapper get(String user) {
        String sessionKey = ServerConstant.SESSION_KEY_PREFIX + user;
        Map<Object, Object> entries = redisTemplate.boundHashOps(sessionKey).entries();
        if (CollectionUtils.isEmpty(entries)) {
            return null;
        }
        return Jackson.toObject(Jackson.toJson(entries), SessionWrapper.class);
    }

    @Override
    public void remove(String user) {
        String sessionKey = ServerConstant.SESSION_KEY_PREFIX + user;
        redisTemplate.delete(sessionKey);
        groupRepository.leaveAll(user);
    }
}
