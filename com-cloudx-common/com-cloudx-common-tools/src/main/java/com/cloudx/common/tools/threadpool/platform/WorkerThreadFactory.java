package com.cloudx.common.tools.threadpool.platform;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 *
 * @author heycm
 * @version 1.0
 * @since 2024/11/22 17:12
 */
public class WorkerThreadFactory implements ThreadFactory {

    private final String name;

    private boolean daemon;

    private final AtomicInteger seq = new AtomicInteger(0);

    public WorkerThreadFactory(String name) {
        this.name = name;
    }

    public WorkerThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, name + "-" + seq.getAndIncrement());
        thread.setDaemon(daemon);
        return thread;
    }
}
