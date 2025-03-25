package cn.heycm.platform.canary.openfeign;

import cn.heycm.platform.canary.openfeign.client.CanaryFeignBlockingLoadBalancerClient;
import cn.heycm.platform.canary.openfeign.interceptor.CanaryHttpInterceptor;
import feign.Client;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Openfeign灰度分发自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/12 21:41
 */
@Configuration
@Slf4j
public class CanaryOpenFeignAutoConfiguration {

    public CanaryOpenFeignAutoConfiguration() {
        log.info("platform component [CanaryOpenFeign] starter ready...");
    }

    /**
     * 传递灰度相关请求头，当前架构仅设计了1层Openfeign远程调用，暂不需要此拦截器
     */
    // @Bean
    public CanaryHttpInterceptor canaryHttpInterceptor() {
        return new CanaryHttpInterceptor();
    }

    /**
     * 负载均衡客户端
     * {@link org.springframework.cloud.openfeign.loadbalancer.DefaultFeignLoadBalancerConfiguration#feignClient(LoadBalancerClient,
     * LoadBalancerClientFactory, List)}
     */
    @Bean
    public Client feignClient(LoadBalancerClient loadBalancerClient, LoadBalancerClientFactory loadBalancerClientFactory,
            List<LoadBalancerFeignRequestTransformer> transformers) {
        return new CanaryFeignBlockingLoadBalancerClient(new Client.Default(null, null), loadBalancerClient, loadBalancerClientFactory,
                transformers);
    }
}
