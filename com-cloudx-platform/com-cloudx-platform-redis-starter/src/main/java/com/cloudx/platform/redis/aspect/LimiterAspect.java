package com.cloudx.platform.redis.aspect;

import com.cloudx.common.entity.constant.AppConstant;
import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.tools.UUIDUtil;
import com.cloudx.platform.redis.annotation.Limiter;
import com.cloudx.platform.redis.client.RedisClient;
import com.cloudx.platform.redis.limiter.IRateLimiter;
import com.cloudx.platform.redis.limiter.SlidingWindowRateLimiter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * 限流处理切面
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:20
 */
@Aspect
@Slf4j
public class LimiterAspect {

    private final IRateLimiter rateLimiter;

    public LimiterAspect(RedisClient redisClient) {
        rateLimiter = new SlidingWindowRateLimiter(redisClient);
    }

    @Around("@annotation(com.cloudx.platform.redis.annotation.Limiter)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limiter limiter = method.getAnnotation(Limiter.class);

        String limitKey = getKey(limiter, method, pjp);
        String requestId = getRequestId();
        boolean allowed = rateLimiter.allowRequest(limitKey, limiter.limit(), limiter.window(), requestId);
        Assert.isTrue(allowed, limiter.error());
        return pjp.proceed();
    }

    private String getRequestId() {
        String traceId = MDC.get(AppConstant.TRACE_ID);
        return StringUtils.hasText(traceId) ? traceId : UUIDUtil.getId();
    }

    private String getKey(Limiter limiter, Method method, ProceedingJoinPoint pjp) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder limitKey = new StringBuilder();
        if (StringUtils.hasText(limiter.prefix())) {
            limitKey.append(limiter.prefix());
        } else {
            limitKey.append(method.getClass().getName()).append("#").append(method.getName());
        }
        if (StringUtils.hasText(limiter.key())) {
            String[] keys = limiter.key().split("\\.");
            Parameter[] parameters = method.getParameters();
            Object[] args = pjp.getArgs();
            String value = getValue(keys, parameters, args);
            if (StringUtils.hasText(value)) {
                limitKey.append(":").append(value);
            }
        }
        return limitKey.toString();
    }

    private String getValue(String[] keys, Parameter[] parameters, Object[] args) throws NoSuchFieldException, IllegalAccessException {
        if (keys == null || parameters == null || args == null) {
            return null;
        }

        Object arg = null;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (i == 0) {
                for (int j = 0; j < parameters.length; j++) {
                    if (key.equals(parameters[j].getName())) {
                        if (args.length <= j) {
                            return null;
                        }
                        arg = args[j];
                        break;
                    }
                }
            } else {
                if (arg == null) {
                    return null;
                }
                Field field = arg.getClass().getDeclaredField(key);
                field.setAccessible(true);
                arg = field.get(arg);
            }
        }

        return arg == null ? null : arg.toString();
    }

}
