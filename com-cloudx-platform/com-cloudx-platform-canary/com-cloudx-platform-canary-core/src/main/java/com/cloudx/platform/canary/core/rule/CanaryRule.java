package com.cloudx.platform.canary.core.rule;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 灰度规则
 * @author heycm
 * @version 1.0
 * @since 2025/3/3 17:31
 */
@Data
public class CanaryRule {

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 标签，用于多路灰度服务隔离
     */
    private String tag;

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 灰度策略
     */
    private CanaryStrategy strategy;

    /**
     * 策略值
     */
    private String canaryValue;

    public boolean check() {
        if (!Boolean.TRUE.equals(enabled)) {
            return false;
        }
        if (!StringUtils.hasText(tag)) {
            return false;
        }
        if (!StringUtils.hasText(serviceId)) {
            return false;
        }
        if (!StringUtils.hasText(path)) {
            return false;
        }
        if (!StringUtils.hasText(canaryValue)) {
            return false;
        }
        if (strategy == null) {
            return false;
        }
        if (strategy == CanaryStrategy.FLOW) {
            try {
                int num = Integer.parseInt(canaryValue);
                return num > 0 && num <= 100;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
