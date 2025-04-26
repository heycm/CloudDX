package com.cloudx.platform.encrypt.repository;

import com.cloudx.platform.encrypt.properties.EncryptProperties;
import java.util.Set;

/**
 * 秘钥存储
 * @author heycm
 * @version 1.0
 * @since 2025/4/25 21:21
 */
public interface SecretRepository {

    void update(EncryptProperties properties);

    EncryptProperties.SecretKey getSecretKey(String tenantId);

    Set<String> getExcludeUrls();

    Set<String> getExcludeContentTypes();
}
