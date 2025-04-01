package com.cloudx.platform.openfeign;

import com.cloudx.platform.openfeign.interceptor.RequestInterceptor;
import com.cloudx.platform.openfeign.interceptor.ResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign 自动装配
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 23:05
 */
@Configuration
@Slf4j
public class OpenFeignAutoConfiguration {

    public OpenFeignAutoConfiguration() {
        log.info("platform component [OpenFeign] starter ready...");
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    // @Bean
    public ResponseInterceptor responseInterceptor() {
        return new ResponseInterceptor();
    }

}
