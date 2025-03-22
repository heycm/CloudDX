package cn.heycm.common.tools.threadpool;

import cn.heycm.common.tools.threadpool.platform.AfterCompleteHandler;
import cn.heycm.common.tools.threadpool.platform.WorkerThreadFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池创建
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 22:44
 */
public class ThreadPoolUtil {

    /**
     * 通用设置
     * @param poolName                 线程池名称
     * @param coreSize                 核心线程数，空闲不销毁
     * @param maxSize                  最大线程数
     * @param queueCapacity            任务队列容量
     * @param keepAliveTime            空闲线程存活时间，单位秒，超时后销毁
     * @param rejectedExecutionHandler 满队时拒绝策略
     * @param afterCompleteHandler     线程执行完成/异常监听处理器
     */
    public static ThreadPoolExecutor create(String poolName, int coreSize, int maxSize, int queueCapacity, int keepAliveTime,
            RejectedExecutionHandler rejectedExecutionHandler, AfterCompleteHandler afterCompleteHandler) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueCapacity),
                new WorkerThreadFactory(poolName), rejectedExecutionHandler) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (afterCompleteHandler != null) {
                    afterCompleteHandler.afterComplete(r, t);
                }
            }
        };
    }

    /**
     * 通用设置
     * @param poolName      线程池名称
     * @param coreSize      核心线程数，空闲不销毁
     * @param maxSize       最大线程数
     * @param queueCapacity 任务队列容量
     * @param keepAliveTime 空闲线程存活时间，单位秒，超时后销毁
     */
    public static ThreadPoolExecutor create(String poolName, int coreSize, int maxSize, int queueCapacity, int keepAliveTime) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueCapacity),
                new WorkerThreadFactory(poolName));
    }
}
