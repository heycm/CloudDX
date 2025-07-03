package com.cloudx.platform.domain.command;

import com.cloudx.common.entity.error.Optional;

/**
 * Create User Handler
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 22:52
 */
public class UserCreateCommandHandler implements CommandHandler<UserCreateCommand, Void> {

    @Override
    public Optional<Void> handle(UserCreateCommand command) {
        return null;
    }
}
