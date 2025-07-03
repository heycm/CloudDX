package com.cloudx.platform.domain.query;

import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.entity.error.Optional;
import com.cloudx.platform.domain.util.ReflectionUtil;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationContext;

/**
 * APP查询总线实现
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 23:17
 */
public class AppQueryBusImpl implements QueryBus {

    private static final int initialCapacity = 256;

    private final ApplicationContext applicationContext;

    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new ConcurrentHashMap<>(initialCapacity);

    public AppQueryBusImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.registerHandlers();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Query<R>> Optional<R> dispatch(T query) {
        Assert.notNull(query, "Query cannot be null");
        QueryHandler<T, R> handler = (QueryHandler<T, R>) queryHandlers.get(query.getClass());
        Assert.notNull(handler, "Query handler not found: " + query.getClass().getName());
        return handler.handle(query);
    }

    @Override
    public <R, T extends Query<R>> CompletableFuture<Optional<R>> dispatchAsync(T query) {
        return null;
    }

    private void registerHandlers() {
        Map<String, QueryHandler> beans = applicationContext.getBeansOfType(QueryHandler.class);
        for (QueryHandler handler : beans.values()) {
            Class<?> queryType = ReflectionUtil.extractQueryType(handler);
            queryHandlers.putIfAbsent(queryType, handler);
        }
    }
}
