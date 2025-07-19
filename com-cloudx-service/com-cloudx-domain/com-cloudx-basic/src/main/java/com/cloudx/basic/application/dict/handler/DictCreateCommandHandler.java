package com.cloudx.basic.application.dict.handler;

import com.cloudx.basic.application.dict.command.DictCreateCommand;
import com.cloudx.common.entity.error.Optional;
import com.cloudx.platform.domain.command.CommandHandler;
import org.springframework.stereotype.Component;

/**
 * 创建字典命令处理器
 * @author heycm
 * @version 1.0
 * @since 2025/7/19 22:01
 */
@Component
public class DictCreateCommandHandler implements CommandHandler<DictCreateCommand, Void> {

    @Override
    public Optional<Void> handle(DictCreateCommand command) {
        return null;
    }
}
