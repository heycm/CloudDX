package com.cloudx.platform.encrypt.repository;

import com.cloudx.platform.encrypt.properties.EncryptProperties;
import com.cloudx.platform.encrypt.properties.EncryptProperties.SecretKey;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 秘钥存储
 * @author heycm
 * @version 1.0
 * @since 2025/4/25 21:51
 */
public class SecretRepositoryImpl implements SecretRepository {

    private final AtomicReference<EncryptProperties> propertiesRef = new AtomicReference<>();

    @Override
    public void update(EncryptProperties properties) {
        propertiesRef.set(properties);
    }

    @Override
    public SecretKey getSecretKey(String tenantId) {
        return propertiesRef.get().getSecretKeys().get(tenantId);
    }

    @Override
    public Set<String> getExcludeUrls() {
        return propertiesRef.get().getExcludeUrls();
    }

    @Override
    public Set<String> getExcludeContentTypes() {
        return propertiesRef.get().getExcludeContentTypes();
    }
}
