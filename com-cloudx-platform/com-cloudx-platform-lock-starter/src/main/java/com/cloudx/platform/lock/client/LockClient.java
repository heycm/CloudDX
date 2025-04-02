package com.cloudx.platform.lock.client;

/**
 * 锁接口定义
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:33
 */
public interface LockClient {

    /**
     * 尝试加锁
     * @param key       锁键
     * @param leaseTime 持有锁时间，毫秒
     * @param waitTime  加锁等待时间，毫秒
     * @return true-加锁成功
     */
    boolean tryLock(String key, long leaseTime, long waitTime);

    /**
     * 释放锁
     */
    void release();
}
