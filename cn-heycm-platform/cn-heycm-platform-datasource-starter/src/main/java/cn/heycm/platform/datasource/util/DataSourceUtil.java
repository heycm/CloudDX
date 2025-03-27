package cn.heycm.platform.datasource.util;

import cn.heycm.common.entity.datasourcce.DataSourceItem;
import cn.heycm.common.entity.datasourcce.DataSourceItemPool;
import cn.heycm.platform.datasource.tenant.TenantDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataSource 工具类
 *
 * @author heycm
 * @version 1.0
 * @since 2024/11/24 15:54
 */
public class DataSourceUtil {

    public static HikariDataSource createDataSource(DataSourceItem item) throws ClassNotFoundException {
        Class.forName(item.getDriverClassName());
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(item.getDriverClassName());
        dataSource.setJdbcUrl(item.getJdbcUrl());
        dataSource.setUsername(item.getUsername());
        dataSource.setPassword(item.getPassword());
        DataSourceItemPool pool = item.getItemPool();
        if (null != pool) {
            dataSource.setPoolName(pool.getPoolName());
            dataSource.setMaximumPoolSize(pool.getMaxPoolSize());
            dataSource.setMinimumIdle(pool.getMinIdle());
            dataSource.setIdleTimeout(pool.getIdleTimeout());
            dataSource.setMaxLifetime(pool.getMaxLifetime());
            dataSource.setConnectionTimeout(pool.getConnectionTimeout());
            dataSource.setAutoCommit(pool.isAutoCommit());
            dataSource.setConnectionTestQuery(pool.getConnectionTestQuery());
            dataSource.setValidationTimeout(pool.getValidationTimeout());
        }
        return dataSource;
    }

    public static TenantDataSource createTenantDataSource(List<DataSourceItem> items) throws ClassNotFoundException {
        TenantDataSource tenantDataSource = new TenantDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (DataSourceItem item : items) {
            HikariDataSource dataSource = createDataSource(item);
            targetDataSources.put(item.getTenantId(), dataSource);
            if (item.isPrimary()) {
                tenantDataSource.setDefaultTargetDataSource(dataSource);
            }
        }
        tenantDataSource.setTargetDataSources(targetDataSources);
        return tenantDataSource;
    }

}
