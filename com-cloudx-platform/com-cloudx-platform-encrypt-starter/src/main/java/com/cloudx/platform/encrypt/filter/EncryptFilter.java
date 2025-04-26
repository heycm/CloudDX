package com.cloudx.platform.encrypt.filter;

import com.cloudx.common.entity.constant.AppConstant;
import com.cloudx.platform.encrypt.properties.EncryptProperties.SecretKey;
import com.cloudx.platform.encrypt.repository.SecretRepository;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 加解密过滤器
 * @author heycm
 * @version 1.0
 * @since 2025/4/25 22:25
 */
public class EncryptFilter implements GlobalFilter, Ordered {

    private final SecretRepository secretRepository;
    private final EncryptRuleMatcher encryptRuleMatcher;
    private final EncryptProcessHandler encryptProcessHandler;

    public EncryptFilter(SecretRepository secretRepository, EncryptRuleMatcher encryptRuleMatcher,
            EncryptProcessHandler encryptProcessHandler) {
        this.secretRepository = secretRepository;
        this.encryptRuleMatcher = encryptRuleMatcher;
        this.encryptProcessHandler = encryptProcessHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String tenantId = request.getHeaders().getFirst(AppConstant.TENANT_ID);
        SecretKey secretKey = secretRepository.getSecretKey(tenantId);
        if (secretKey == null || !secretKey.isEnable()) {
            return chain.filter(exchange);
        }
        if (encryptRuleMatcher.shouldSkip(request)) {
            return chain.filter(exchange);
        }

        ServerHttpRequest decryptedRequest = encryptProcessHandler.decryptRequest(request, secretKey);
        ServerHttpResponse encryptedResponse = encryptProcessHandler.encryptResponse(exchange.getResponse(), secretKey);
        return chain.filter(exchange.mutate()
                .request(decryptedRequest)
                .response(encryptedResponse)
                .build());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
