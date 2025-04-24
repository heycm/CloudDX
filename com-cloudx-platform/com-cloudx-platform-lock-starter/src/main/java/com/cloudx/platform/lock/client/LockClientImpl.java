package com.cloudx.platform.lock.client;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 锁接口实现
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 22:45
 */
@Slf4j
public class LockClientImpl implements LockClient {

    private static final String PREFIX = "distributed:lock:";

    private static final ThreadLocal<RLock> LOCKS = new ThreadLocal<>();

    private final RedissonClient redissonClient;

    public LockClientImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLock(String key, long leaseTime, long waitTime) {
        try {
            key = PREFIX + key;
            RLock lock = redissonClient.getLock(key);
            LOCKS.set(lock);
            boolean success = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (log.isDebugEnabled()) {
                log.debug("LockClient try lock [{}] result: {}", key, success);
            }
            return success;
        } catch (InterruptedException e) {
            log.error("LockClient try lock [{}] error: {}", key, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void release() {
        RLock lock = LOCKS.get();
        if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
            LOCKS.remove();
            lock.unlock();
            if (log.isDebugEnabled()) {
                log.debug("LockClient release lock [{}]", lock.getName());
            }
            return;
        }
        if (lock != null) {
            log.warn("LockClient lock is invalid, not need to release.");
        }
    }
}
