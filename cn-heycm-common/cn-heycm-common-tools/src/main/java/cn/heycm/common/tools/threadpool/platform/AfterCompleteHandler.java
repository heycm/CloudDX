package cn.heycm.common.tools.threadpool.platform;

/**
 * 执行单元后置处理接口/异常处理接口
 * @author heycm
 * @version 1.0
 * @since 2024/11/22 17:13
 */
public interface AfterCompleteHandler {

    /**
     * 线程执行完成/异常监听方法，子线程执行完成或发生异常时触发
     * @param r 执行单元
     * @param t 异常信息（若发生）
     */
    void afterComplete(Runnable r, Throwable t);
}
