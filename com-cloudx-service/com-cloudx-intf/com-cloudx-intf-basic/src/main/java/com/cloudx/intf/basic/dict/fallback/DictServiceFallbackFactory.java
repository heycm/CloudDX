package com.cloudx.intf.basic.dict.fallback;

import com.cloudx.common.entity.response.Result;
import com.cloudx.intf.basic.dict.IDictService;
import com.cloudx.intf.basic.dict.dto.DictCreateRequest;
import com.cloudx.intf.basic.dict.dto.DictModifyRequest;
import com.cloudx.platform.openfeign.error.FeignError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 字典服务降级
 * @author heycm
 * @version 1.0
 * @since 2025/7/18 0:05
 */
@Component
@Slf4j
public class DictServiceFallbackFactory implements FallbackFactory<IDictService> {

    @Override
    public IDictService create(Throwable cause) {
        log.error("字典服务降级: {}", cause.getMessage(), cause);
        return new IDictService() {
            @Override
            public Result<?> create(DictCreateRequest request) {
                return FeignError.explain(cause);
            }

            @Override
            public Result<?> modify(DictModifyRequest request) {
                return FeignError.explain(cause);
            }
        };
    }
}
