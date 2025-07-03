package com.cloudx.platform.domain.command;

import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.entity.error.Optional;
import com.cloudx.platform.domain.util.ReflectionUtil;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * APP命令总线实现
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 21:40
 */
@Component
public class AppCommandBusImpl implements CommandBus {

    private static final int initialCapacity = 256;

    private final ApplicationContext applicationContext;

    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new ConcurrentHashMap<>(initialCapacity);

    public AppCommandBusImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.registerHandlers();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Command<R>> Optional<R> dispatch(T command) {
        Assert.notNull(command, "Command cannot be null");
        CommandHandler<T, R> handler = (CommandHandler<T, R>) commandHandlers.get(command.getClass());
        Assert.notNull(handler, "Command handler not found: " + command.getClass().getName());
        return handler.handle(command);
    }

    @Override
    public <R, T extends Command<R>> CompletableFuture<Optional<R>> dispatchAsync(T command) {
        return null;
    }

    private void registerHandlers() {
        Map<String, CommandHandler> beans = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler handler : beans.values()) {
            Class<?> commandClass = ReflectionUtil.extractCommandType(handler);
            commandHandlers.putIfAbsent(commandClass, handler);
        }
    }
}
