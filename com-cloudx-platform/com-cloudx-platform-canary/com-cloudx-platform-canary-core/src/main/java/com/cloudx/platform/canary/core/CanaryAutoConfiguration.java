package com.cloudx.platform.canary.core;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.canary.core.cache.CanaryRuleCache;
import com.cloudx.platform.canary.core.rule.CanaryRule;
import com.cloudx.platform.nacos.service.NacosConfListener;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * 流量分发组件自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/4 14:20
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients(defaultConfiguration = CanaryLoadBalancerClientConfiguration.class)
@EnableConfigurationProperties(CanaryProperties.class)
public class CanaryAutoConfiguration {

    public CanaryAutoConfiguration(CanaryProperties canaryProperties, NacosConfListener nacosConfListener) {
        log.info("platform component [Canary] starter ready...");
        nacosConfListener.addListener(canaryProperties.getDatId(), canaryProperties.getGroupId(), config -> {
            List<CanaryRule> rules = Jackson.toList(config, CanaryRule.class);
            CanaryRuleCache.refresh(rules);
        });
    }

}
