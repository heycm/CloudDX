package com.cloudx.platform.apisign.builder;

import com.cloudx.common.tools.cipher.MD5Util;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@Accessors(chain = true)
@Setter
public class SignBuilder {

    private static final char DELIMITER = '#';

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 签名密钥
     */
    private String secretKey;

    /**
     * 请求头携带随机字符
     */
    private String nonce;

    /**
     * 请求头携带时间戳
     */
    private String timestamp;

    /**
     * 请求路由
     */
    private String uri;

    /**
     * 请求路由参数
     */
    private String query;

    /**
     * 请求载荷
     */
    private String payload;

    /**
     * 构建签名：MD5(uri + ? + query + # + payload + # + timestamp + # + nonce + # + tenantId + # + secretKey)
     * @return 签名
     */
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append(uri);
        if (StringUtils.hasText(query)) {
            builder.append("?").append(query);
        }
        if (StringUtils.hasText(payload)) {
            builder.append(DELIMITER).append(payload);
        }
        builder.append(DELIMITER).append(timestamp);
        builder.append(DELIMITER).append(nonce);
        builder.append(DELIMITER).append(tenantId);
        builder.append(DELIMITER).append(secretKey);
        String raw = builder.toString();
        String sign = MD5Util.encode(raw);
        if (log.isDebugEnabled()) {
            log.debug("Signature raw: {}, md5: {}", raw, sign);
        }
        return sign;
    }
}
