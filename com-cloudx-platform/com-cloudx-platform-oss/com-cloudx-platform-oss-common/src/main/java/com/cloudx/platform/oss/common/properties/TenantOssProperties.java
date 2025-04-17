package com.cloudx.platform.oss.common.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 多租户OSS节点配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 16:33
 */
@Data
public class TenantOssProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -5945102360663711280L;

    private Map<String, OssProperties> tenant;
}
