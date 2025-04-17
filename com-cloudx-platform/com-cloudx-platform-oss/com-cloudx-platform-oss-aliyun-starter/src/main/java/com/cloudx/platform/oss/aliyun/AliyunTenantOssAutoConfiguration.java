package com.cloudx.platform.oss.aliyun;

import com.cloudx.platform.oss.aliyun.factory.AliyunOssServiceFactory;
import com.cloudx.platform.oss.aliyun.service.AliyunOssServiceTenantAdapter;
import com.cloudx.platform.oss.common.properties.TenantOssProperties;
import com.cloudx.platform.oss.common.service.OssService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AliyunTenantOssAutoConfiguration {

    public AliyunTenantOssAutoConfiguration() {
        log.info("platform component [AliyunTenantOss] starter ready...");
    }

    @Bean
    @ConfigurationProperties(prefix = "oss.aliyun.tenant")
    public TenantOssProperties tenantOssProperties() {
        return new TenantOssProperties();
    }

    @Bean
    public AliyunOssServiceFactory aliyunOssServiceFactory() {
        return new AliyunOssServiceFactory();
    }

    @Bean
    public OssService aliyunOssServiceTenantAdapter(TenantOssProperties tenantOssProperties,
            AliyunOssServiceFactory aliyunOssServiceFactory) {
        return new AliyunOssServiceTenantAdapter(tenantOssProperties, aliyunOssServiceFactory);
    }
}
