package com.cloudx.platform.lock.client;

import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.entity.error.CodeMsg;
import com.cloudx.platform.lock.function.LockRunnable;

/**
 * 编程式分布式锁
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 23:21
 */
public final class LockX {

    private static volatile LockClient lockClient;

    private static final String PREFIX = "distributed:lock:";

    private LockX() {
    }

    public static void initLockClient(LockClient lockClient) {
        if (LockX.lockClient == null) {
            synchronized (LockX.class) {
                if (LockX.lockClient == null) {
                    LockX.lockClient = lockClient;
                }
            }
        }
    }

    /**
     * 获取锁成功并执行
     * @param runnable 执行单元
     * @param key      锁key
     */
    public static void lock(Runnable runnable, String key) {
        LockX.lock(runnable, key, 3000, 300);
    }

    /**
     * 获取锁成功并执行
     * @param runnable  执行单元
     * @param key       锁key
     * @param leaseTime 锁过期时间
     * @param waitTime  等待时间
     */
    public static void lock(Runnable runnable, String key, long leaseTime, long waitTime) {
        if (lockClient == null) {
            throw new NullPointerException("LockClient is null");
        }
        try {
            key = PREFIX + key;
            boolean success = lockClient.tryLock(key, leaseTime, waitTime);
            Assert.isTrue(success, CodeMsg.LOCK_FAILED);
            runnable.run();
        } finally {
            lockClient.release();
        }
    }

    /**
     * 获取锁成功并执行
     * @param runnable  执行单元
     * @param key       锁key
     * @param leaseTime 锁过期时间
     * @param waitTime  等待时间
     */
    public static <T> T apply(LockRunnable<T> runnable, String key, long leaseTime, long waitTime) throws Throwable {
        if (lockClient == null) {
            throw new NullPointerException("LockClient is null");
        }
        try {
            key = PREFIX + key;
            boolean success = lockClient.tryLock(key, leaseTime, waitTime);
            Assert.isTrue(success, CodeMsg.LOCK_FAILED);
            return runnable.run();
        } finally {
            lockClient.release();
        }
    }

}
