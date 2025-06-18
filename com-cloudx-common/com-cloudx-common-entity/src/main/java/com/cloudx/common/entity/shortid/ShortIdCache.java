package com.cloudx.common.entity.shortid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 短ID缓存
 * @author heycm
 * @version 1.0
 * @since 2025/6/18 22:58
 */
public final class ShortIdCache {

    private static final Map<String, AtomicInteger> NEXT_ID = new ConcurrentHashMap<>();
    private static final Map<String, Integer> MAX_ID = new ConcurrentHashMap<>();
    private static final Map<String, Lock> IDKEY_LOCK = new ConcurrentHashMap<>();

    /**
     * 获取下一个短ID
     * @param idKey    短ID键
     * @param provider 短ID生成服务
     * @return 下一个短ID
     */
    public static int nextId(String idKey, ShortIdProvider provider) {
        Lock lock = IDKEY_LOCK.computeIfAbsent(idKey, k -> new ReentrantLock());
        lock.lock();
        AtomicInteger nextId = NEXT_ID.get(idKey);
        Integer maxId = MAX_ID.get(idKey);
        try {
            if (nextId == null || nextId.get() > maxId) {
                ShortId shortId = provider.getNextId(idKey).data();
                nextId = new AtomicInteger(shortId.getNextId());
                maxId = shortId.getMaxId();
                NEXT_ID.put(idKey, nextId);
                MAX_ID.put(idKey, maxId);
            }
        } finally {
            lock.unlock();
        }
        return nextId.getAndIncrement();
    }
}
