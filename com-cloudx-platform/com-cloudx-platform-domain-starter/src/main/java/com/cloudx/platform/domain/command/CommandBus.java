package com.cloudx.platform.domain.command;

import com.cloudx.common.entity.error.Optional;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * 命令总线，负责传递 Command 到对应 CommandHandler
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 21:21
 */
public interface CommandBus {

    /**
     * 下发命令
     * @param command 命令
     * @param <R>     命令处理结果
     * @param <T>     命令
     * @return 命令处理结果
     */
    <R, T extends Command<R>> Optional<R> dispatch(T command);

    /**
     * 异步下发命令
     * @param command 命令
     * @param <R>     命令处理结果
     * @param <T>     命令
     * @return 命令处理结果
     */
    <R, T extends Command<R>> CompletableFuture<Optional<R>> dispatchAsync(T command);

    /**
     * 异步下发命令
     * @param command 命令
     * @param timeout 超时时间
     * @param <R>     命令处理结果
     * @param <T>     命令
     * @return 命令处理结果
     */
    <R, T extends Command<R>> CompletableFuture<Optional<R>> dispatchAsync(T command, Duration timeout);

}
