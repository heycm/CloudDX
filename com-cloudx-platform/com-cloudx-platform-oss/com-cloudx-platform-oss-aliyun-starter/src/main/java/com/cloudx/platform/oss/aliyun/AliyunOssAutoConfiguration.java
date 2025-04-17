package com.cloudx.platform.oss.aliyun;

import com.cloudx.platform.oss.aliyun.factory.AliyunOssServiceFactory;
import com.cloudx.platform.oss.common.properties.OssProperties;
import com.cloudx.platform.oss.common.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:38
 */
@Configuration
@ConditionalOnProperty(value = "oss.aliyun.endpoint")
@EnableConfigurationProperties
@Slf4j
public class AliyunOssAutoConfiguration {

    public AliyunOssAutoConfiguration() {
        log.info("platform component [AliyunOss] starter ready...");
    }

    @Bean
    @ConfigurationProperties(prefix = "oss.aliyun")
    public OssProperties ossProperties() {
        return new OssProperties();
    }

    @Bean
    public AliyunOssServiceFactory aliyunOssServiceFactory() {
        return new AliyunOssServiceFactory();
    }

    @Bean
    public OssService aliyunOssService(OssProperties ossProperties, AliyunOssServiceFactory aliyunOssServiceFactory) {
        return aliyunOssServiceFactory.create(ossProperties);
    }
}
