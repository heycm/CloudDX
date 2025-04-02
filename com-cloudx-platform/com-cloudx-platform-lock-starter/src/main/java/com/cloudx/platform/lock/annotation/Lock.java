package com.cloudx.platform.lock.annotation;

import com.cloudx.common.entity.error.CodeMsg;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 锁注解
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 22:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * 锁前缀
     */
    String prefix() default "";

    /**
     * 解析动态锁key值，格式：[参数名].[字段名]
     */
    String key();

    /**
     * 锁过期时间，毫秒
     */
    long leaseTime() default 3000;

    /**
     * 获取锁最大等待时间，毫秒
     */
    long waitTime() default 300;

    /**
     * 加锁失败时返回的错误码
     */
    CodeMsg error() default CodeMsg.LOCK_FAILED;

}
