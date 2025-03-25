package cn.heycm.platform.canary.gateway;

import cn.heycm.platform.canary.gateway.filter.CanaryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 灰度网关自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/12 21:31
 */
@Configuration
@Slf4j
public class CanaryGatewayAutoConfiguration {

    public CanaryGatewayAutoConfiguration() {
        log.info("platform component [CanaryGateway] starter ready...");
    }

    @Bean
    public CanaryFilter canaryFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new CanaryFilter(clientFactory, properties);
    }

}
