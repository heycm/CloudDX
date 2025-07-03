package com.cloudx.platform.domain;

import com.cloudx.platform.domain.command.CommandHandler;
import com.cloudx.platform.domain.command.UserCreateCommandHandler;
import com.cloudx.platform.domain.util.ReflectionUtil;

/**
 * Test
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 22:51
 */
public class Test {

    public static void main(String[] args) {
        CommandHandler handler = new UserCreateCommandHandler();
        Class<?> commandType = ReflectionUtil.extractCommandType(handler);
        System.out.println(commandType);
    }

}
