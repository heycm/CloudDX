package cn.heycm.platform.canary.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nacos 灰度配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/3 17:17
 */
@Data
@ConfigurationProperties(prefix = "spring.cloud.loadbalancer.canary-config")
public class CanaryProperties {

    /**
     * 灰度规则文件名
     */
    private String datId = "micro-canary-rule.json";

    /**
     * 灰度规则分组
     */
    private String groupId = "CANARY_GROUP";
}
