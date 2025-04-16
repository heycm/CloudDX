package com.cloudx.platform.apisign.repository;

import com.cloudx.platform.apisign.properties.SignProperties;

import java.util.List;

/**
 * 密钥存储层
 */
public interface SignRepository {

    /**
     * 保存租户签名配置
     * @param properties
     */
    void saveProperties(SignProperties properties);

    /**
     * 保存租户签名配置
     * @param properties
     */
    void saveProperties(List<SignProperties> properties);

    /**
     * 获取租户签名配置
     * @param tenantId 租户ID
     * @return 配置信息
     */
    SignProperties getProperties(String tenantId);
}
