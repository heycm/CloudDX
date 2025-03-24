package cn.heycm.platform.openfeign.interceptor;

import cn.heycm.common.entity.constant.AppConstant;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求拦截，设置请求头
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 23:09
 */
public class RequestInterceptor implements feign.RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Web 请求头
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if (AppConstant.TOKEN_HEADER.equalsIgnoreCase(name)) {
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                    if (AppConstant.TERMINAL.equalsIgnoreCase(name)) {
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
            }
        }

        // 链路追踪
        setTraceId(requestTemplate);
    }

    private void setTraceId(RequestTemplate requestTemplate) {
        String traceId = MDC.get(AppConstant.TRACE_ID);
        if (traceId != null) {
            requestTemplate.header(AppConstant.TRACE_ID, traceId);
        }
        String uid = MDC.get(AppConstant.UID);
        if (uid != null) {
            requestTemplate.header(AppConstant.UID, uid);
        }
    }
}
