package com.cloudx.platform.oss.aliyun;

import com.cloudx.platform.oss.common.properties.OssProperties;
import java.util.HashMap;
import java.util.Map;
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
@ConditionalOnProperty(value = "oss.aliyun")
@EnableConfigurationProperties
public class AliyunOSSAutoConfiguration {


    @Bean
    @ConditionalOnProperty(value = "oss.aliyun.endpoint")
    @ConfigurationProperties(prefix = "oss.aliyun")
    public OssProperties ossProperties() {
        return new OssProperties();
    }

    @Bean
    @ConditionalOnProperty(value = "oss.aliyun.tenant")
    @ConfigurationProperties(prefix = "oss.aliyun.tenant")
    public Map<String, OssProperties> tenantOssProperties() {
        return new HashMap<>();
    }
}
