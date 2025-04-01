package com.cloudx.common.entity.error;

/**
 * 服务异常
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:09
 */
public class ServiceException extends RuntimeException {

    public final int code;

    public ServiceException(String message) {
        super(message);
        code = CodeMsg.ERROR.code();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.message());
        code = errorCode.code();
    }
}
