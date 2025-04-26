package com.cloudx.platform.encrypt.filter;

import com.cloudx.platform.encrypt.repository.SecretRepository;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 规则匹配
 * @author heycm
 * @version 1.0
 * @since 2025/4/25 22:09
 */
public class EncryptRuleMatcher {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final SecretRepository secretRepository;

    public EncryptRuleMatcher(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    public boolean shouldSkip(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        String contentType = request.getHeaders().getContentType().toString();
        return secretRepository.getExcludeContentTypes().contains(contentType) || secretRepository.getExcludeUrls().stream()
                .allMatch(pattern -> pathMatcher.match(pattern, path));
    }

}
