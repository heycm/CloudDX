package com.cloudx.platform.encrypt.properties;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.Data;

/**
 * 加解密配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/24 18:54
 */
@Data
public class EncryptProperties {

    /**
     * 密钥
     */
    private Map<String, SecretKey> secretKeys;

    /**
     * 排除的url
     */
    private Set<String> excludeUrls;

    /**
     * 排除的contentType
     */
    private Set<String> excludeContentTypes;

    @Data
    public static class SecretKey {

        /**
         * 租户id
         */
        private String tenantId;

        /**
         * 是否启用
         */
        private boolean enable;

        /**
         * 密钥
         */
        private String secretKey;

        /**
         * 密钥偏移量
         */
        private String secretIv;
    }

    public Map<String, SecretKey> getSecretKeys() {
        return secretKeys == null ? Collections.emptyMap() : secretKeys;
    }

    public Set<String> getExcludeUrls() {
        return excludeUrls == null ? Collections.emptySet() : excludeUrls;
    }

    public Set<String> getExcludeContentTypes() {
        return excludeContentTypes == null ? Collections.emptySet() : excludeContentTypes;
    }
}
