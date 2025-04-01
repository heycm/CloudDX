package com.cloudx.platform.datasource.aspect;

import com.cloudx.common.entity.constant.AppConstant;
import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.entity.tenant.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 租户切面，设置租户上下文，设置切面最高优先级（否则与事务注解在同一个方法使用时会失效）
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:19
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TenantAspect {

    @Around("@annotation(com.cloudx.platform.datasource.annotation.Tenant)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String tenantId = this.getRequestTenantId();
        Assert.notBlank(tenantId, "Tenant context is missing.");
        try {
            TenantContextHolder.setTenantId(tenantId);
            return joinPoint.proceed();
        } finally {
            TenantContextHolder.clear();
        }
    }

    /**
     * 获取请求头中租户ID
     */
    private String getRequestTenantId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(AppConstant.TENANT_ID);
    }
}
