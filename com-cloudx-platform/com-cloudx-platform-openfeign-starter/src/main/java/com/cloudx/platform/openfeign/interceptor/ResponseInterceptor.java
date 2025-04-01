package com.cloudx.platform.openfeign.interceptor;

import feign.InvocationContext;

/**
 * 响应拦截器
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 23:15
 */
public class ResponseInterceptor implements feign.ResponseInterceptor {

    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {
        return chain.next(invocationContext);
    }
}
