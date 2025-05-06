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
     * session缓存：websocket:session:{sessionId} -> session
     */
    private static final String SESSION_KEY_PREFIX = "websocket:session:";
    
    /**
     * user缓存：websocket:user:{userId} -> sessionId
     */
    private static final String USER_KEY_PREFIX = "websocket:user:";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionReposiyory(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(SessionWrapper session) {
        String sessionKey = SESSION_KEY_PREFIX + session.getSessionId();
        Map m = Jackson.toObject(Jackson.toJson(session), Map.class);
        redisTemplate.boundHashOps(sessionKey).putAll(m);
        String userKey = USER_KEY_PREFIX + session.getUserId();
        redisTemplate.boundValueOps(userKey).set(session.getSessionId());
    }

    @Override
    public SessionWrapper getSession(String sessionId) {
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        Map<Object, Object> entries = redisTemplate.boundHashOps(sessionKey).entries();
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
        return getSession(o.toString());
    }

    @Override
    public void removeSession(String sessionId) {
        SessionWrapper session = getSession(sessionId);
        if (session != null) {
            String sessionKey = SESSION_KEY_PREFIX + sessionId;
            redisTemplate.delete(sessionKey);
            String userKey = USER_KEY_PREFIX + session.getUserId();
            redisTemplate.delete(userKey);
        }
    }

    @Override
    public void removeUser(String userId) {
        String userKey = USER_KEY_PREFIX + userId;
        Object o = redisTemplate.boundValueOps(userKey).get();
        if (o != null) {
            redisTemplate.delete(List.of(userKey, SESSION_KEY_PREFIX + o.toString()));
        }
    }
}
