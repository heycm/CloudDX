package com.cloudx.common.web.filter;

import com.cloudx.common.web.filter.wrapper.CachedRequestWrapper;
import com.cloudx.common.web.filter.wrapper.CachedResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 请求响应装饰，支持重复读取请求体和响应体
 * @author hey
 * @version 1.0
 * @since 2024/12/5 18:08
 */
@Component
@Order(0)
public class RequestCacheFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (this.skipWrapper(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        CachedRequestWrapper requestWrapper = new CachedRequestWrapper(request);
        CachedResponseWrapper responseWrapper = new CachedResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.writeToClient();
    }

    private boolean skipWrapper(HttpServletRequest request) {
        if (skipPath(request)) {
            return true;
        }
        // 获取请求的 Content-Type
        String contentType = request.getContentType();
        // 检查 Content-Type 是否包含 "multipart/form-data"
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

    private boolean skipPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        return pathMatcher.match("/**/actuator/**", path) || pathMatcher.match("/**/static/**", path);
    }
}
