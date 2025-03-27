package cn.heycm.platform.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 租户注解，使用在 MQ 监听事件上，解析 MQ 消息中的租户信息并设置租户数据源
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EventTenant {}
