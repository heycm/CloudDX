package com.cloudx.common.entity.response;

import com.cloudx.common.entity.error.CodeMsg;
import com.cloudx.common.entity.error.ErrorCode;
import com.cloudx.common.entity.error.Optional;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 统一响应模型
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:12
 */
@Data
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1625142293747871158L;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应成功
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = CodeMsg.SUCCESS.code();
        result.message = CodeMsg.SUCCESS.message();
        return result;
    }

    /**
     * 响应成功
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = Result.success();
        result.data = data;
        return result;
    }

    /**
     * 响应失败
     * @param code    响应码
     * @param message 响应消息
     */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }

    /**
     * 响应失败
     * @param message 响应消息
     */
    public static <T> Result<T> error(String message) {
        return Result.error(CodeMsg.ERROR.code(), message);
    }

    /**
     * 响应失败
     * @param errorCode 响应码
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return Result.error(errorCode.code(), errorCode.message());
    }

    /**
     * 解析 Optional 响应成功或失败
     * @param optional optional
     */
    public static <T> Result<T> optional(Optional<T> optional) {
        if (optional.isPresent()) {
            return Result.success(optional.get());
        }
        return Result.error(optional.code(), optional.message());
    }

    /**
     * 是否成功
     * @return true-成功
     */
    public boolean isSuccess() {
        return CodeMsg.SUCCESS.code() == code;
    }
}
