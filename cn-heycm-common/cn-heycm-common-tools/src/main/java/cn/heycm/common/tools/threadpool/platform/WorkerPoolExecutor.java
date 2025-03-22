package cn.heycm.common.tools.threadpool.platform;

import cn.heycm.common.tools.threadpool.ThreadPoolUtil;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 业务线程池
 * @author heycm
 * @version 1.0
 * @since 2024/11/22 17:15
 */
public class WorkerPoolExecutor {

    /**
     * 默认线程池名称
     */
    private static final String DEFAULT_WORKER_POOL_NAME = "worker-pool";

    /**
     * 线程池
     */
    private final ThreadPoolExecutor executor;

    public WorkerPoolExecutor(String workerPoolName, int coreSize, int maxSize, int queueCapacity, int keepAliveTime,
            RejectedExecutionHandler rejectedExecutionHandler, AfterCompleteHandler afterCompleteHandler) {
        executor = ThreadPoolUtil.create(workerPoolName, coreSize, maxSize, queueCapacity, keepAliveTime, rejectedExecutionHandler,
                afterCompleteHandler);
    }

    public WorkerPoolExecutor(int coreSize, int maxSize, int queueCapacity, int keepAliveTime,
            RejectedExecutionHandler rejectedExecutionHandler, AfterCompleteHandler afterCompleteHandler) {
        executor = ThreadPoolUtil.create(DEFAULT_WORKER_POOL_NAME, coreSize, maxSize, queueCapacity, keepAliveTime);
    }

    /**
     * 执行任务
     * @param task 任务单元
     */
    public void execute(Runnable task) {
        executor.execute(task);
    }

    /**
     * 执行任务，返回结果
     * @param task 任务单元
     * @return 结果
     */
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    /**
     * 执行任务
     * @param task   任务单元
     * @param result 任务上下文
     * @return 返回传递的 result 上下文信息
     */
    public <T> Future<T> submit(Runnable task, T result) {
        return executor.submit(task, result);
    }
}
