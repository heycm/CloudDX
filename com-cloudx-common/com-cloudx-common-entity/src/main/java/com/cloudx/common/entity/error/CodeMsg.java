package com.cloudx.common.entity.error;

/**
 * 响应码枚举
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:13
 */
public enum CodeMsg implements ErrorCode {

    // 成功
    SUCCESS(200, "成功"),

    // 错误/失败/异常
    ERROR(500, "系统繁忙，请稍后再试"),

    // ---- 系统级错误码 ---------------------------------------------
    // 参数校验失败
    INVALID_PARAM(400, "参数错误"),

    // 权限认证失败
    ERROR_AUTHENTICATION(401, "认证失败"),

    // 权限检查失败
    ERROR_AUTHORIZATION(403, "权限不足"),

    // 找不到路由资源
    RESOURCE_NOT_FOUND(404, "资源不存在"),

    // RPC无服务
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // RPC降级
    SERVICE_FALLBACK(503, "服务降级"),

    // RPC超时
    SERVICE_TIMEOUT(504, "服务超时"),

    // 服务超时
    GATEWAY_TIMEOUT(504, "网关超时"),

    // Sentinel 限流
    ERROR_BLOCKED_SENTINEL(1071, "Blocked by Sentinel"),

    // Sentinel 限流
    ERROR_BLOCKED_FLOW(1072, "服务限流"),

    // Sentinel 限流
    ERROR_BLOCKED_DEGRADE(1073, "服务降级"),

    // Sentinel 限流
    ERROR_BLOCKED_PARAM(1074, "热点参数限流"),

    // Sentinel 限流
    ERROR_BLOCKED_SYSTEM(1075, "系统异常"),

    // Sentinel 限流
    ERROR_BLOCKED_AUTHORITY(1076, "授权异常"),

    // QPS限制
    TOO_MANY_REQUEST(1080, "服务拥挤，请稍后再试"),

    // ---- 通用业务错误码 ---------------------------------------------
    // 参数缺失
    PARAM_MISSING(2001, "参数缺失"),

    // 不存在相应的数据信息
    NO_DATA(2002, "不存在相应的数据信息"),

    // 相应的数据信息保存失败
    SAVE_FAILED(2003, "相应的数据信息保存失败"),

    // 相应的数据信息删除失败
    DEL_FAILED(2004, "相应的数据信息删除失败"),

    // 不支持媒体文件类型
    MEDIA_NOT_SUPPORT(2005, "不支持媒体文件类型"),

    // 文件上传失败
    UPLOAD_FAILED(2006, "文件上传失败"),

    // 锁定资源失败
    LOCK_FAILED(2007, "锁定资源失败")


    // ---- Basic业务错误码: 11000 ---------------------------------------------
    // ---- User业务错误码: 12000 ---------------------------------------------
    // ---- Order业务错误码: 13000 ---------------------------------------------
    // ---- Pay业务错误码: 14000 ---------------------------------------------

    ;

    private final int code;

    private final String message;

    CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
