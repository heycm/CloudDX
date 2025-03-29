package cn.heycm.platform.datasource.aspect;

import cn.heycm.common.entity.constant.AppConstant;
import cn.heycm.common.entity.error.Assert;
import cn.heycm.common.entity.tenant.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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

    @Pointcut("@annotation(cn.heycm.platform.datasource.annotation.Tenant)")
    public void tenantCut() {
    }

    @Pointcut("@annotation(cn.heycm.platform.datasource.annotation.EventTenant)")
    public void eventTenantCut() {
    }

    @Around("tenantCut()")
    public Object tenantAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String tenantId = this.getRequestTenantId();
        return this.around(tenantId, joinPoint);
    }

    @Around("eventTenantCut()")
    public Object eventTenantAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String tenantId = this.getEventTenantId(joinPoint);
        return this.around(tenantId, joinPoint);
    }

    private Object around(String tenantId, ProceedingJoinPoint joinPoint) throws Throwable {
        Assert.notBlank(tenantId, "Tenant context is missing.");
        if (TenantContextHolder.alreadySet()) {
            log.debug("Tenant context switches from [{}] to [{}]", TenantContextHolder.getTenantId(), tenantId);
        } else {
            log.debug("Tenant context switches to [{}]", tenantId);
        }
        try {
            TenantContextHolder.setTenantId(tenantId);
            return joinPoint.proceed();
        } finally {
            if (TenantContextHolder.alreadySet()) {
                log.debug("Tenant context [{}] removed.", TenantContextHolder.getTenantId());
                TenantContextHolder.clear();
            } else {
                log.warn("Tenant context has already been removed, Please check the nested use of @Tenant or @EventTenant.");
            }
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

    /**
     * 获取 MQ 监听事件中租户ID
     * @param joinPoint
     * @return
     */
    private String getEventTenantId(ProceedingJoinPoint joinPoint) {
        return null;
    }
}
