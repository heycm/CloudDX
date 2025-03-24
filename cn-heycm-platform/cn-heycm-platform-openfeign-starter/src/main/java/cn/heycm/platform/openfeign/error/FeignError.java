package cn.heycm.platform.openfeign.error;

import cn.heycm.common.entity.error.CodeMsg;
import cn.heycm.common.entity.response.Result;
import feign.FeignException.ServiceUnavailable;
import feign.RetryableException;

/**
 * 异常解析
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 23:16
 */
public class FeignError {

    public static <T> Result<T> explain(Throwable throwable) {
        if (throwable instanceof RetryableException) {
            return Result.error(CodeMsg.SERVICE_TIMEOUT);
        }
        if (throwable instanceof ServiceUnavailable) {
            return Result.error(CodeMsg.SERVICE_UNAVAILABLE);
        }
        return Result.error(CodeMsg.SERVICE_FALLBACK);
    }

}
