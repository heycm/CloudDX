package com.cloudx.platform.datasource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 租户注解，从请求头中获取租户ID并设置租户数据源
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Tenant {
}
