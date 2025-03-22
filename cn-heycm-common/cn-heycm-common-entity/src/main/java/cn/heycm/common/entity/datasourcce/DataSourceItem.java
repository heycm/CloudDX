package cn.heycm.common.entity.datasourcce;

import java.text.MessageFormat;
import lombok.Data;

/**
 * 数据源配置项
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:00
 */
@Data
public class DataSourceItem {

    public static final MessageFormat JDBC_FORMAT = new MessageFormat(
            "jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai");

    /**
     * 租户
     */
    private String tenant;

    /**
     * 是否主（默认）数据源
     */
    private boolean primary = false;

    /**
     * 驱动类
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库连接地址
     */
    private String host;

    /**
     * 数据库端口
     */
    private int port = 3306;

    /**
     * 数据库名称
     */
    private String schema;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 连接池配置
     */
    private DataSourceItemPool itemPool;

    /**
     * 获取jdbc连接地址
     * @return JDBC URL
     */
    public String getJdbcUrl() {
        return JDBC_FORMAT.format(new Object[]{host, port + "", schema});
    }
}
