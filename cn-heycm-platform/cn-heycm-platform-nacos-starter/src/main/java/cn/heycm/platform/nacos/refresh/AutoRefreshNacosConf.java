package cn.heycm.platform.nacos.refresh;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 动态刷新配置，监听 application.yml 中的 nacosconf.xxx 配置项，在配置中心修改后，会自动刷新到该对象中
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:06
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "nacosconf")
public class AutoRefreshNacosConf {

    /**
     * 此类可作为自动刷新配置的基类，可以设定一些通用配置，如：debuger 等
     */
    private boolean debuger;

}
