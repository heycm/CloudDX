package cn.heycm.platform.datasource.util;

import cn.heycm.common.entity.datasourcce.DataSourceItem;
import cn.heycm.common.entity.datasourcce.DataSourceItemPool;
import com.zaxxer.hikari.HikariDataSource;

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

}
