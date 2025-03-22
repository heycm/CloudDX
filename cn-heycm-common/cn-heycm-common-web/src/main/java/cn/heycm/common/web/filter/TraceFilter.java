package cn.heycm.common.web.filter;

import cn.heycm.common.entity.constant.AppConstant;
import cn.heycm.common.tools.UUIDUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 设置链路ID
 * @author hey
 * @version 1.0
 * @since 2024/12/5 17:55
 */
@Component
@Order(Integer.MIN_VALUE)
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 处理请求时，从请求头中获取链路ID，如果没有则生成，并将其添加到MDC中，以便后续日志追踪请求
        try {
            setMDC(request);
            filterChain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    private void setMDC(HttpServletRequest request) {
        String traceId = request.getHeader(AppConstant.TRACE_ID);
        traceId = StringUtils.hasText(traceId) ? traceId : UUIDUtil.getId();
        MDC.put(AppConstant.TRACE_ID, traceId);
        String uid = request.getHeader(AppConstant.UID);
        if (!"0".equals(uid)) {
            MDC.put(AppConstant.UID, uid);
        }
    }

    private void clearMDC() {
        MDC.remove(AppConstant.TRACE_ID);
        MDC.remove(AppConstant.UID);
    }
}
