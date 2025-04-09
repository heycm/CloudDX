package com.cloudx.platform.apisign.filter;

import com.cloudx.platform.apisign.repository.NonceRepository;
import com.cloudx.platform.apisign.repository.SignRepository;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 签名校验过滤器
 */
public class SignFilter implements WebFilter {

    private final SignRepository signRepository;
    private final NonceRepository nonceRepository;

    public SignFilter(SignRepository signRepository, NonceRepository nonceRepository) {
        this.signRepository = signRepository;
        this.nonceRepository = nonceRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return null;
    }
}
