package com.cloudx.platform.canary.core.request;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;

/**
 * 包装灰度请求，指定缓存请求头
 * @author heycm
 * @version 1.0
 * @since 2025/3/10 22:37
 */
@Getter
public class CanaryRequest<T> extends DefaultRequest<T> {

    private Map<String, Object> extHeader;

    public CanaryRequest(T context) {
        super(context);
    }

    public void addExtHeader(String name, Object value) {
        if (extHeader == null) {
            extHeader = new HashMap<>();
        }
        extHeader.put(name, value);
    }
}
