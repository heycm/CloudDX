package com.cloudx.platform.encrypt;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.encrypt.filter.EncryptFilter;
import com.cloudx.platform.encrypt.filter.EncryptProcessHandler;
import com.cloudx.platform.encrypt.filter.EncryptRuleMatcher;
import com.cloudx.platform.encrypt.properties.EncryptProperties;
import com.cloudx.platform.encrypt.repository.SecretRepository;
import com.cloudx.platform.encrypt.repository.SecretRepositoryImpl;
import com.cloudx.platform.nacos.service.NacosConfListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口加解密自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/24 18:17
 */
@Configuration
@Slf4j
public class EncryptAutoConfiguration {

    private static final String GROUP = "ENCRYPT";

    private static final String DATA_ID = "api-encrypt.json";

    private final NacosConfListener nacosConfListener;

    public EncryptAutoConfiguration(NacosConfListener nacosConfListener) {
        this.nacosConfListener = nacosConfListener;
        log.info("platform component [Encrypt] starter ready...");
    }

    @Bean
    public SecretRepository secretRepository() {
        SecretRepositoryImpl repository = new SecretRepositoryImpl();
        nacosConfListener.addListener(DATA_ID, GROUP, config -> {
            EncryptProperties properties = Jackson.toObject(config, EncryptProperties.class);
            repository.update(properties);
        });
        return repository;
    }

    @Bean
    public EncryptRuleMatcher encryptRuleMatcher(SecretRepository secretRepository) {
        return new EncryptRuleMatcher(secretRepository);
    }

    @Bean
    public EncryptProcessHandler encryptProcessHandler() {
        return new EncryptProcessHandler();
    }

    @Bean
    public EncryptFilter encryptFilter(SecretRepository secretRepository, EncryptRuleMatcher encryptRuleMatcher, EncryptProcessHandler encryptProcessHandler) {
        return new EncryptFilter(secretRepository, encryptRuleMatcher, encryptProcessHandler);
    }
}
