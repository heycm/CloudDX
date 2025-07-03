package com.cloudx.platform.domain.command;

import com.cloudx.common.entity.error.Optional;

/**
 * 命令处理器
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 21:09
 */
public interface CommandHandler<T extends Command<R>, R> {

    Optional<R> handle(T command);

}
