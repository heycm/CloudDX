package cn.heycm.platform.datasource;

import cn.heycm.common.entity.datasourcce.DataSourceItem;
import cn.heycm.common.entity.error.Assert;
import cn.heycm.common.tools.Jackson;
import cn.heycm.platform.datasource.aspect.TenantAspect;
import cn.heycm.platform.datasource.properties.DataSourceProperties;
import cn.heycm.platform.datasource.util.DataSourceUtil;
import cn.heycm.platform.nacos.service.NacosConfListener;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 数据源自动配置
 * @author heycm
 * @version 1.0
 * @since 2024/11/24 15:12
 */
@Configuration
@ConditionalOnProperty(value = "datasource.schema")
@EnableConfigurationProperties({DataSourceProperties.class})
@Slf4j
public class DataSourceAutoConfiguration {

    private static final String DATA_ID = "micro-datasource.json";

    private List<DataSourceItem> dataSourceItems;

    public DataSourceAutoConfiguration(NacosConfListener nacosConfListener, DataSourceProperties properties) {
        Assert.notBlank(properties.getSchema(), "Properties of datasource.schema is blank.");
        nacosConfListener.addListener(DATA_ID, (config) -> {
            List<DataSourceItem> list = Jackson.toList(config, DataSourceItem.class);
            list = list == null ? Collections.emptyList() : list;
            dataSourceItems = list.stream().filter(item -> properties.getSchema().equals(item.getSchema())).toList();
            Assert.isTrue(!dataSourceItems.isEmpty(), "Can not find datasource config by schema: " + properties.getSchema());
        });
        log.info("platform component [DataSource] starter ready...");
    }

    /**
     * 注册数据源
     * @return dataSource
     */
    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        return DataSourceUtil.createTenantDataSource(dataSourceItems);
    }

    /**
     * 注册事务管理器
     * @param dataSource 数据源
     * @return transactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 注册租户数据源切面
     */
    @Bean
    public TenantAspect tenantAspect() {
        return new TenantAspect();
    }
}
