package com.cloudx.platform.sms.aliyun;

import com.cloudx.platform.sms.aliyun.service.AliyunSmsServiceImpl;
import com.cloudx.platform.sms.common.properties.SmsProperties;
import com.cloudx.platform.sms.common.service.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 21:45
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "sms.aliyun.access-key")
public class AliyunSmsAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "sms.aliyun")
    public SmsProperties smsProperties() {
        return new SmsProperties();
    }

    @Bean
    @ConditionalOnMissingBean(name = "aliyunSmsService")
    public SmsService aliyunSmsService(SmsProperties smsProperties) throws Exception {
        return new AliyunSmsServiceImpl(smsProperties);
    }
}
