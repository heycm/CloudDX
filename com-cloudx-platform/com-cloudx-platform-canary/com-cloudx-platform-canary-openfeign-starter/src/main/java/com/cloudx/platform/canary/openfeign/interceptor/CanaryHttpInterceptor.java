package com.cloudx.platform.canary.openfeign.interceptor;

import com.cloudx.platform.canary.core.chooser.CanaryRuleChooser;
import com.cloudx.platform.canary.core.rule.CanaryStrategy;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign Canary 请求头传递
 * @author heycm
 * @version 1.0
 * @since 2025/3/5 17:22
 */
public class CanaryHttpInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if (CanaryRuleChooser.CANARY_TAG_HEADER.equals(name) || CanaryStrategy.HEAD.getHeaderName().equals(name)
                        || CanaryStrategy.IP.getHeaderName().equals(name) || CanaryStrategy.FLOW.getHeaderName().equals(name)) {
                    String values = request.getHeader(name);
                    template.header(name, values);
                }
            }
        }
    }
}
