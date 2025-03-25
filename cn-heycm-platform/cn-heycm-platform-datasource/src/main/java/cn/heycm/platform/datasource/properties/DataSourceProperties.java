package cn.heycm.platform.datasource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据源配置
 * @author heycm
 * @version 1.0
 * @since 2024/11/24 17:52
 */
@Data
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties {

    /**
     * 数据库名称
     */
    private String schema;

}
