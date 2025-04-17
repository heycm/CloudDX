package com.cloudx.platform.oss.aliyun;

import com.cloudx.platform.oss.common.properties.TenantOssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多租户OSS配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 16:43
 */
@Configuration
@ConditionalOnProperty(value = "oss.aliyun.tenant")
@EnableConfigurationProperties
public class AliyunTenantOssAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "oss.aliyun.tenant")
    public TenantOssProperties tenantOssProperties() {
        return new TenantOssProperties();
    }

}
