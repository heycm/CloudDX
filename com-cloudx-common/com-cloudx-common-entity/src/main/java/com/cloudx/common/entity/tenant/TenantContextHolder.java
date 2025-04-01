package com.cloudx.common.entity.tenant;

import lombok.extern.slf4j.Slf4j;

/**
 * 租户上下文
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 20:54
 */
@Slf4j
public class TenantContextHolder {

    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        if (exists()) {
            log.debug("Tenant context switches from [{}] to [{}]", TenantContextHolder.getTenantId(), tenantId);
        } else {
            log.debug("Tenant context switches to [{}]", tenantId);
        }
        TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return TENANT.get();
    }

    public static void clear() {
        if (exists()) {
            log.debug("Tenant context [{}] removed.", TenantContextHolder.getTenantId());
            TENANT.remove();
        } else {
            log.warn("Tenant context has already been removed, Please check the nested set tenant.");
        }
    }

    public static boolean exists() {
        return TENANT.get() != null;
    }
}
