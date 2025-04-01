package com.cloudx.common.tools.threadpool.virtual;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * 虚拟线程：适合IO密集型任务
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 22:10
 */
public class VirtualThread {

    public static final ExecutorService EXECUTOR;

    static {
        ThreadFactory factory = Thread.ofVirtual().name("vThread-", 1).factory();
        EXECUTOR = Executors.newThreadPerTaskExecutor(factory);
    }

    public static void execute(Runnable command) {
        EXECUTOR.execute(command);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return EXECUTOR.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result) {
        return EXECUTOR.submit(task, result);
    }
}
