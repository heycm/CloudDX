package com.cloudx.platform.domain.query;

import com.cloudx.common.entity.error.Optional;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * 查询总线
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 23:14
 */
public interface QueryBus {

    /**
     * 下发查询
     * @param query 查询
     * @param <R>   查询处理结果
     * @param <T>   查询
     * @return 查询处理结果
     */
    <R, T extends Query<R>> Optional<R> dispatch(T query);

    /**
     * 异步下发查询
     * @param query 查询
     * @param <R>   查询处理结果
     * @param <T>   查询
     * @return 查询处理结果
     */
    <R, T extends Query<R>> CompletableFuture<Optional<R>> dispatchAsync(T query);

    /**
     * 异步下发查询
     * @param query   查询
     * @param timeout 超时时间
     * @param <R>     查询处理结果
     * @param <T>     查询
     * @return 查询处理结果
     */
    <R, T extends Query<R>> CompletableFuture<Optional<R>> dispatchAsync(T query, Duration timeout);

}
