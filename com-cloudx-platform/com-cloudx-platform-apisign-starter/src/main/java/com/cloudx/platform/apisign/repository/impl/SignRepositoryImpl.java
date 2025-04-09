package com.cloudx.platform.apisign.repository.impl;

import com.cloudx.platform.apisign.properties.SignProperties;
import com.cloudx.platform.apisign.repository.SignRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 签名秘钥存储实现
 * @author heycm
 * @version 1.0
 * @since 2025/4/9 22:24
 */
public class SignRepositoryImpl implements SignRepository {

    private final Map<String, SignProperties> data = new ConcurrentHashMap<>();

    @Override
    public void saveProperties(SignProperties properties) {
        data.put(properties.getTenantId(), properties);
    }

    @Override
    public SignProperties getProperties(String tenantId) {
        SignProperties properties = data.get(tenantId);
        if (properties == null) {
            properties = new SignProperties();
            properties.setTenantId(tenantId);
        }
        return properties;
    }
}
