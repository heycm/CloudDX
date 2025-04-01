package com.cloudx.platform.mq.consumer.multicaster;

import com.cloudx.common.entity.constant.AppConstant;
import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.entity.tenant.TenantContextHolder;
import com.cloudx.platform.mq.common.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件转播处理中心
 * @author heycm
 * @version 1.0
 * @since 2025/04/01 17:40
 */
@Slf4j
public class EventMulticaster {

    /**
     * @EventHandler 标记的MQ消息处理器对象: eventName -> Handler
     */
    private static final Map<String, Object> HANDLERS = new ConcurrentHashMap<>();

    /**
     * @EventHandler 标记的MQ消息处理器对象方法: eventName -> Method
     */
    private static final Map<String, Method> METHODS = new ConcurrentHashMap<>();

    /**
     * 注册事件处理器
     * @param handlers
     */
    public static void registerHandlers(List<Object> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            log.warn("Not found any EventHandler.");
            return;
        }
        handlers.forEach(EventMulticaster::registerHandler);
    }

    /**
     * 注册事件处理器
     * @param handler
     */
    public static void registerHandler(Object handler) {
        Class<?> clazz = handler.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            String eventName = method.getName();
            Assert.isTrue(!METHODS.containsKey(eventName), "Event [" + eventName + "] has already registered in: " + HANDLERS.get(eventName).getClass().getName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            boolean parameterCheck = parameterTypes.length == 1 && Event.class.isAssignableFrom(parameterTypes[0]);
            Assert.isTrue(parameterCheck, "Event handler method [" + eventName + "] must has one parameter of type Event");
            METHODS.put(eventName, method);
            HANDLERS.put(eventName, handler);
            log.info("Event [{}] register success.", eventName);
        }
    }

    /**
     * 消息转播到处理器
     * @param event
     */
    public static void onEvent(Event event) {
        try {
            setTrace(event);
            String topic = event.getTopic();
            String eventName = event.getEvent();
            Method method = METHODS.get(eventName);
            Object handler = HANDLERS.get(eventName);
            if (method == null || handler == null) {
                log.warn("Topic [{}] not support event: [{}]", topic, eventName);
                return;
            }
            TenantContextHolder.setTenantId(event.getTenantId());
            method.invoke(handler, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            TenantContextHolder.clear();
            clearTrace(event);
        }
    }

    private static void clearTrace(Event event) {
        MDC.remove(AppConstant.TRACE_ID);
        MDC.remove(AppConstant.UID);
        MDC.remove(AppConstant.TENANT_ID);
    }

    private static void setTrace(Event event) {
        MDC.put(AppConstant.TRACE_ID, event.getEventId());
        MDC.put(AppConstant.UID, event.getEventUid());
        MDC.put(AppConstant.TENANT_ID, event.getTenantId());
    }
}
