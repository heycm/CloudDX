package com.cloudx.platform.apisign.filter;

import com.cloudx.common.entity.constant.AppConstant;
import com.cloudx.common.entity.error.Assert;
import com.cloudx.platform.apisign.builder.SignBuilder;
import com.cloudx.platform.apisign.properties.SignProperties;
import com.cloudx.platform.apisign.repository.NonceRepository;
import com.cloudx.platform.apisign.repository.SignRepository;
import java.util.concurrent.TimeUnit;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 签名校验过滤器
 */
public class SignFilter implements WebFilter, Ordered {

    private final SignRepository signRepository;
    private final NonceRepository nonceRepository;

    private final long timeout = 1000 * 60 * 5;

    public SignFilter(SignRepository signRepository, NonceRepository nonceRepository) {
        this.signRepository = signRepository;
        this.nonceRepository = nonceRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String tenantId = headers.getFirst(AppConstant.TENANT_ID);
        SignProperties properties = signRepository.getProperties(tenantId);
        if (!properties.isEnable()) {
            return chain.filter(exchange);
        }

        String signature = headers.getFirst(AppConstant.X_SIGNATURE);
        String nonce = headers.getFirst(AppConstant.X_NONCE);
        String timestamp = headers.getFirst(AppConstant.X_TIMESTAMP);
        String uri = request.getURI().getPath();
        String query = request.getURI().getQuery();

        // 验签
        String sign = new SignBuilder().setTenantId(tenantId).setSecretKey(properties.getSecretKey()).setUri(uri).setQuery(query)
                .setNonce(nonce).setTimestamp(timestamp).build();
        Assert.isTrue(sign.equals(signature), "Invalid signature.");

        // 校验时间戳
        Assert.isTrue(System.currentTimeMillis() - Long.parseLong(timestamp) < timeout, "Invalid signature timestamp.");

        // 校验随机字符
        boolean saved = nonceRepository.saveIfAbsent(tenantId, nonce, timeout, TimeUnit.MILLISECONDS);
        Assert.isTrue(saved, "Invalid signature nonce.");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
