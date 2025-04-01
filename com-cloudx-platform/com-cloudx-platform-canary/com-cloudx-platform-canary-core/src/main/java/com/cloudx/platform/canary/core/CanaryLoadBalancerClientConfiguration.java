package com.cloudx.platform.canary.core;

import com.cloudx.platform.canary.core.loadbalancer.CanaryRoundRobinLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 负载均衡配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/5 17:13
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class CanaryLoadBalancerClientConfiguration {

    /**
     * 注册自定义负载均衡器，替换默认 {@link org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer}
     * <p>
     * 参考: {@link org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration}
     * </p>
     */
    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        log.info("[Canary] 服务: {}, 注册负载均衡器...", name);
        return new CanaryRoundRobinLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }

}
