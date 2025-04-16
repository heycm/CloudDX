package com.cloudx.platform.apisign.repository.impl;

import com.cloudx.platform.apisign.repository.PathRepository;
import org.springframework.util.AntPathMatcher;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 18:48
 */
public class PathRepositoryImpl implements PathRepository {

    private final AtomicReference<Set<String>> patternsRef = new AtomicReference<>();
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void save(Set<String> pathPatterns) {
        patternsRef.set(pathPatterns);
    }

    @Override
    public boolean isMatchExclusion(String path) {
        return patternsRef.get().stream().anyMatch(pattern -> matcher.match(pattern, path));
    }
}
