package com.cloudx.common.web.filter;

import com.cloudx.common.tools.Jackson;
import com.cloudx.common.web.filter.wrapper.CachedRequestWrapper;
import com.cloudx.common.web.filter.wrapper.CachedResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 自动请求响应日志打印，以便调试定位问题
 * @author hey
 * @version 1.0
 * @since 2024/12/5 18:11
 */
@Component
@Order(1)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!(request instanceof CachedRequestWrapper requestWrapper) || !(response instanceof CachedResponseWrapper responseWrapper)) {
            filterChain.doFilter(request, response);
            return;
        }
        StopWatch sw = new StopWatch();
        sw.start();

        String method = requestWrapper.getMethod();
        String path = requestWrapper.getRequestURI();

        Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
        if (!CollectionUtils.isEmpty(parameterMap)) {
            log.info("请求: {} {} Parameter: {}", method, path, Jackson.toJson(parameterMap));
        }
        if (!requestWrapper.isEmptyBody()) {
            String param = Jackson.toJson(Jackson.toObject(requestWrapper.getContentAsString(), Map.class));
            log.info("请求: {} {} Body: {}", method, path, param);
        }
        if (CollectionUtils.isEmpty(parameterMap) && requestWrapper.isEmptyBody()) {
            log.info("请求: {} {}", method, path);
        }

        filterChain.doFilter(requestWrapper, responseWrapper);

        sw.stop();
        log.info("耗时: {}ms, 响应: {}", sw.getTotalTimeMillis(), responseWrapper.getContentAsString());
    }
}
