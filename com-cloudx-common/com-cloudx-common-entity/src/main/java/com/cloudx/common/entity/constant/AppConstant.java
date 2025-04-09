package com.cloudx.common.entity.constant;

/**
 * 系统全局常量池
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:07
 */
public interface AppConstant {

    // 链路ID
    String TRACE_ID = "TraceId";

    // 用户ID
    String UID = "UID";

    // 请求头Token
    String TOKEN_HEADER = "Authorization";

    // Token前缀
    String TOKEN_PREFIX = "Bearer ";

    // 来源终端
    String TERMINAL = "Terminal";

    // 请求头租户ID
    String TENANT_ID = "TenantId";

    // 请求时间戳
    String X_TIMESTAMP = "X-Timestamp";

    // 请求随机字符
    String X_NONCE = "X-Nonce";

    // 请求签名
    String X_SIGNATURE = "X-Signature";
}
