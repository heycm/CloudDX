package com.cloudx.intf.basic.dict;

import com.cloudx.intf.basic.dict.fallback.DictServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 字典服务
 * @author heycm
 * @version 1.0
 * @since 2025/7/18 0:01
 */
@FeignClient(name = "domain-basic", path = "/domain/basic/dict", fallbackFactory = DictServiceFallbackFactory.class)
public interface DictService extends IDictService {}
