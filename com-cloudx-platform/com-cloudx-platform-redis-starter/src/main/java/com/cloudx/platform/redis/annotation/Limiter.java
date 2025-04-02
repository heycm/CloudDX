package com.cloudx.platform.redis.annotation;

import com.cloudx.common.entity.error.CodeMsg;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

    /**
     * key 前缀，默认为：[类名#方法名]
     */
    String prefix() default "";

    /**
     * 解析动态 key 值，格式：[参数名].[字段名]
     */
    String key() default "";

    /**
     * 时间窗口，默认 1 秒
     */
    int window() default 1;

    /**
     * 时间窗口内限制数量，默认 1 个
     */
    int limit() default 1;

    /**
     * 限流时错误码
     */
    CodeMsg error() default CodeMsg.TOO_MANY_REQUEST;

}
