package cn.heycm.platform.datasource.tenant;

import cn.heycm.common.entity.tenant.TenantContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 租户数据源
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 20:56
 */
public class TenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenantId();
    }
}
