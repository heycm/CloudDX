package com.cloudx.platform.canary.core.rule;

import lombok.Getter;

/**
 * 灰度策略
 * @author heycm
 * @version 1.0
 * @since 2025/3/3 17:27
 */
@Getter
public enum CanaryStrategy {

    /**
     * 根据特定请求头分发灰度
     */
    HEAD("X-Canary-Id"),

    /**
     * 根据IP网段分发灰度
     */
    IP("X-Canary-IP"),

    /**
     * 根据流量比率分发灰度
     */
    FLOW("X-Canary-Flow");

    /**
     * 灰度请求携带请求头名称
     */
    private final String headerName;

    CanaryStrategy(String headerName) {
        this.headerName = headerName;
    }
}
