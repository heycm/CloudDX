package com.cloudx.platform.apisign.repository.impl;

import com.cloudx.platform.apisign.properties.SignProperties;
import com.cloudx.platform.apisign.repository.SignRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
