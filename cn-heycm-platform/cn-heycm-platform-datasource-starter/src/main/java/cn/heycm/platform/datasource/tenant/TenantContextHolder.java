package cn.heycm.platform.datasource.tenant;

import org.springframework.util.StringUtils;

/**
 * 租户上下文
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 20:54
 */
public class TenantContextHolder {

    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return TENANT.get();
    }

    public static void clear() {
        TENANT.remove();
    }

    public static boolean alreadySet() {
        return StringUtils.hasText(TENANT.get());
    }
}
