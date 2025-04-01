package com.cloudx.platform.nacos;

import com.cloudx.platform.nacos.refresh.AutoRefreshNacosConf;
import com.cloudx.platform.nacos.service.NacosConfListener;
import com.cloudx.platform.nacos.service.NacosConfPublisher;
import com.cloudx.platform.nacos.service.impl.NacosConfListenerImpl;
import com.cloudx.platform.nacos.service.impl.NacosConfPublisherImpl;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Nacos自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:02
 */
@Configuration
@EnableConfigurationProperties({AutoRefreshNacosConf.class})
@Slf4j
public class NacosAutoConfiguration {

    public NacosAutoConfiguration() {
        log.info("platform component [Nacos] starter ready...");
    }

    @Bean
    public NacosConfListener nacosConfListener(ConfigService configService) {
        return new NacosConfListenerImpl(configService);
    }

    @Bean
    public NacosConfPublisher nacosConfPublisher(ConfigService configService) {
        return new NacosConfPublisherImpl(configService);
    }
}
