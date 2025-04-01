package com.cloudx.common.entity.datasourcce;

import lombok.Data;

/**
 * 数据源连接池
 * @author heycm
 * @version 1.0
 * @since 2025-3-22 19:03
 */
@Data
public class DataSourceItemPool {

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * 最大连接数
     */
    private int maxPoolSize = 10;

    /**
     * 最小空闲连接数
     */
    private int minIdle = 10;

    /**
     * 空闲连接超时时间（毫秒），超时将关闭连接
     */
    private long idleTimeout = 600000;

    /**
     * 连接最大生命周期（毫秒），超时将强制关闭重建
     */
    private long maxLifetime = 1800000;

    /**
     * 获取连接超时时间（毫秒）
     */
    private long connectionTimeout = 300L;

    /**
     * 是否自动提交
     */
    private boolean autoCommit = true;

    /**
     * 测试连接有效性语句
     */
    private String connectionTestQuery = "select 1";

    /**
     * 测试连接有效性超时时间（毫秒）
     */
    private long validationTimeout = 5000L;
}
