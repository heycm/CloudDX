package com.cloudx.platform.websocket.core.repository.impl;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
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

    /**
     * channel缓存：websocket:channel:{channelId} -> session
     */
    private static final String CHANNEL_KEY_PREFIX = "websocket:channel:";
    
    /**
     * user缓存：websocket:user:{userId} -> channelId
     */
    private static final String USER_KEY_PREFIX = "websocket:user:";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionReposiyory(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(SessionWrapper session) {
        String channelKey = CHANNEL_KEY_PREFIX + session.getChannelId();
        Map m = Jackson.toObject(Jackson.toJson(session), Map.class);
        redisTemplate.boundHashOps(channelKey).putAll(m);
        String userKey = USER_KEY_PREFIX + session.getUserId();
        redisTemplate.boundValueOps(userKey).set(session.getChannelId());
    }

    @Override
    public SessionWrapper getChannel(String channelId) {
        String channelKey = CHANNEL_KEY_PREFIX + channelId;
        Map<Object, Object> entries = redisTemplate.boundHashOps(channelKey).entries();
        if (CollectionUtils.isEmpty(entries)) {
            return null;
        }
        return Jackson.toObject(Jackson.toJson(entries), SessionWrapper.class);
    }

    @Override
    public SessionWrapper getUser(String userId) {
        String userKey = USER_KEY_PREFIX + userId;
        Object o = redisTemplate.boundValueOps(userKey).get();
        if (o == null) {
            return null;
        }
        return getChannel(o.toString());
    }

    @Override
    public void removeChannel(String channelId) {
        SessionWrapper channel = getChannel(channelId);
        if (channel != null) {
            String channelKey = CHANNEL_KEY_PREFIX + channelId;
            redisTemplate.delete(channelKey);
            String userKey = USER_KEY_PREFIX + channel.getUserId();
            redisTemplate.delete(userKey);
        }
    }

    @Override
    public void removeUser(String userId) {
        String userKey = USER_KEY_PREFIX + userId;
        Object o = redisTemplate.boundValueOps(userKey).get();
        if (o != null) {
            redisTemplate.delete(List.of(userKey, CHANNEL_KEY_PREFIX + o.toString()));
        }
    }
}
