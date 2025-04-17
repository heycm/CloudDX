package com.cloudx.platform.oss.common.service;

/**
 * 多租户OSS服务适配
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 16:15
 */
public interface OssServiceTenantAdapter extends OssService {

    /**
     * 获取租户ID
     * @return OSS
     */
    OssService getTenantOssService();

}
