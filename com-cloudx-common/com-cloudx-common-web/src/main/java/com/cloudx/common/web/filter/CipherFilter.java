package com.cloudx.common.web.filter;

import com.cloudx.common.entity.response.Result;
import com.cloudx.common.tools.Jackson;
import com.cloudx.common.tools.cipher.AESUtil;
import com.cloudx.common.web.filter.wrapper.CachedRequestWrapper;
import com.cloudx.common.web.filter.wrapper.CachedResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 加解密，仅使用在 APP 层
 * @author heycm
 * @version 1.0
 * @since 2024/12/21 2:31
 */
// @Component
@Order(2)
@Slf4j
public class CipherFilter extends OncePerRequestFilter {

    // 需要加解密的字段
    private static final String[] FIELDS = new String[]{"username", "phoneId", "idCard"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!(request instanceof CachedRequestWrapper requestWrapper) || !(response instanceof CachedResponseWrapper responseWrapper)) {
            filterChain.doFilter(request, response);
            return;
        }

        requestHandle(requestWrapper);
        filterChain.doFilter(requestWrapper, responseWrapper);
        responseHandle(responseWrapper);
    }

    private void requestHandle(CachedRequestWrapper requestWrapper) {
        Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
        if (!CollectionUtils.isEmpty(parameterMap)) {
            log.debug("Request Parameter 加密处理前: {}", parameterMap);
            for (String field : FIELDS) {
                String[] value = parameterMap.get(field);
                if (value != null && value.length > 0) {
                    value[0] = AESUtil.encrypt(value[0]);
                    requestWrapper.addParameter(field, value);
                }
            }
            log.debug("Request Parameter 加密处理后: {}", parameterMap);
        }

        Map body = Jackson.toObject(requestWrapper.getContentAsString(), Map.class);
        if (!CollectionUtils.isEmpty(body)) {
            log.debug("Request Body 加密处理前: {}", body);
            for (String field : FIELDS) {
                Object value = body.get(field);
                if (value != null) {
                    body.put(field, AESUtil.encrypt(value.toString()));
                }
            }
            log.debug("Request Body 加密处理后: {}", body);
            requestWrapper.setBody(Jackson.toBytes(body));
        }
    }

    private void responseHandle(CachedResponseWrapper responseWrapper) throws IOException {
        Result result = Jackson.toObject(responseWrapper.getContentAsString(), Result.class);
        if (result == null) {
            return;
        }
        if (!result.isSuccess() || result.getData() == null) {
            return;
        }
        log.debug("Response Body 解密处理前: {}", Jackson.toJson(result));
        boolean resetBody = false;
        Object data = result.getData();
        if (data instanceof Map<?, ?> map) {
            if (map.containsKey("page") && map.containsKey("size") && map.containsKey("list")) {
                // 符合 PageResult 特征
                List list = (List) map.get("list");
                resetBody = decryptCollection(list);
            } else {
                resetBody = decryptMap(map);
            }
        } else if (data instanceof Collection<?> collection) {
            resetBody = decryptCollection(collection);
        }
        log.debug("Response Body 解密处理后: {}", Jackson.toJson(result));
        if (resetBody) {
            resetResponseBody(responseWrapper, result);
        }
    }

    private void resetResponseBody(CachedResponseWrapper responseWrapper, Object data) throws IOException {
        byte[] body = Jackson.toBytes(data);
        responseWrapper.setBody(body);
    }

    private boolean decryptMap(Map map) {
        boolean hasField = false;
        for (String field : FIELDS) {
            Object data = map.get(field);
            if (data != null) {
                map.put(field, AESUtil.decrypt(data.toString()));
                hasField = true;
            }
        }
        return hasField;
    }

    private boolean decryptCollection(Collection collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }
        Object first = collection.stream().findFirst().get();
        if (!(first instanceof Map<?, ?>)) {
            return false;
        }
        boolean hasField = false;
        Map fMap = (Map) first;
        for (String field : FIELDS) {
            if (fMap.containsKey(field)) {
                hasField = true;
                break;
            }
        }
        if (!hasField) {
            return false;
        }
        for (Object o : collection) {
            Map map = (Map) o;
            for (String field : FIELDS) {
                Object data = map.get(field);
                if (data != null) {
                    map.put(field, AESUtil.decrypt(data.toString()));
                }
            }
        }
        return true;
    }
}
