package com.cloudx.platform.apisign.repository;

import java.util.Set;

/**
 * URL缓存
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 18:44
 */
public interface PathRepository {

    void save(Set<String> pathPatterns);

    boolean isMatchExclusion(String path);
}
