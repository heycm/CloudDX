package com.cloudx.platform.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.WebFluxCallbackManager;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.cloudx.common.entity.error.CodeMsg;
import com.cloudx.common.entity.response.Result;
import com.cloudx.common.tools.IOUtil;
import com.cloudx.common.tools.Jackson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Sentinel 自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/23 18:03
 */
@Configuration
@ConditionalOnProperty(value = "spring.cloud.sentinel.transport.dashboard")
public class SentinelAutoConfiguration {

    /**
     * WebMVC 模块
     */
    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    @ConditionalOnMissingClass("org.springframework.web.reactive.DispatcherHandler")
    public BlockExceptionHandler blockExceptionHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            Result<?> result = converResult(e);
            IOUtil.writeJson(httpServletResponse, result);
        };
    }

    /**
     * WebFlux 模块
     */
    @Bean
    @ConditionalOnClass(DispatcherHandler.class)
    public BlockRequestHandler blockRequestHandler() {
        BlockRequestHandler handler = (serverWebExchange, throwable) -> {
            Result<?> result = converResult(throwable);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Jackson.toJson(result));
        };
        WebFluxCallbackManager.setBlockHandler(handler);
        return handler;
    }

    private static Result<?> converResult(Throwable throwable) {
        return switch (throwable) {
            case FlowException ignored -> Result.error(CodeMsg.ERROR_BLOCKED_FLOW);
            case DegradeException ignored -> Result.error(CodeMsg.ERROR_BLOCKED_DEGRADE);
            case ParamFlowException ignored -> Result.error(CodeMsg.ERROR_BLOCKED_PARAM);
            case SystemBlockException ignored -> Result.error(CodeMsg.ERROR_BLOCKED_SYSTEM);
            case AuthorityException ignored -> Result.error(CodeMsg.ERROR_BLOCKED_AUTHORITY);
            case null, default -> Result.error(CodeMsg.ERROR_BLOCKED_SENTINEL);
        };
    }
}
