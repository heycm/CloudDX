package com.cloudx.platform.oss.common.factory;

import com.cloudx.platform.oss.common.properties.OssProperties;
import com.cloudx.platform.oss.common.service.OssService;

/**
 * OSS服务创建工厂
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 16:14
 */
public interface OssServiceFactory {

    OssService create(OssProperties properties);

}
