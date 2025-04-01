package com.cloudx.common.entity.error;

/**
 * 错误码标准
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:08
 */
public interface ErrorCode {

    /**
     * 错误码
     */
    int code();

    /**
     * 错误信息
     */
    String message();
}
