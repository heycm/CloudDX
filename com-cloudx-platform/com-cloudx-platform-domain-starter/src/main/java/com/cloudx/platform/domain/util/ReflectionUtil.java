package com.cloudx.platform.domain.util;

import com.cloudx.platform.domain.command.CommandHandler;
import com.cloudx.platform.domain.query.QueryHandler;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;

/**
 * 反射工具
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 22:55
 */
public class ReflectionUtil {

    public static Class<?> extractCommandType(CommandHandler handlerBean) {
        // 获取SpringAOP代理的目标类
        Class<?> targetClass = AopUtils.getTargetClass(handlerBean);
        // 解析目标类的泛型参数
        ResolvableType resolvableType = ResolvableType.forClass(targetClass).as(CommandHandler.class);
        if (resolvableType == ResolvableType.NONE) {
            throw new IllegalArgumentException("Invalid CommandHandler interface: " + targetClass.getName());
        }
        Class<?> commandType = resolvableType.getGeneric(0).resolve();
        if (commandType == null) {
            throw new IllegalArgumentException("Invalid CommandHandler generic CommandType : " + targetClass.getName());
        }
        return commandType;
    }

    public static Class<?> extractQueryType(QueryHandler handlerBean) {
        // 获取SpringAOP代理的目标类
        Class<?> targetClass = AopUtils.getTargetClass(handlerBean);
        // 解析目标类的泛型参数
        ResolvableType resolvableType = ResolvableType.forClass(targetClass).as(QueryHandler.class);
        if (resolvableType == ResolvableType.NONE) {
            throw new IllegalArgumentException("Invalid QueryHandler interface: " + targetClass.getName());
        }
        Class<?> queryType = resolvableType.getGeneric(0).resolve();
        if (queryType == null) {
            throw new IllegalArgumentException("Invalid QueryHandler generic QueryType : " + targetClass.getName());
        }
        return queryType;
    }
}
