package com.cloudx.common.tools.threadpool.task;

/**
 * 任务回调
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 22:26
 */
public interface TaskCallback {

    <T> void onSuccess(T result);

    <T> void onFailure(Throwable e);
}
