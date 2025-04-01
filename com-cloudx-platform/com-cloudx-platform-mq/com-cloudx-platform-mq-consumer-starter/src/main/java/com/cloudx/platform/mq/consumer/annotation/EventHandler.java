package com.cloudx.platform.mq.consumer.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息事件处理
 * @author heycm
 * @version 1.0
 * @since 2025/04/01 17:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EventHandler {

    @AliasFor(annotation = Component.class) String value() default "";
}
