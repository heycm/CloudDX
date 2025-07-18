package com.cloudx.basic.adapter.intf.dict;

import com.cloudx.common.entity.response.Result;
import com.cloudx.intf.basic.dict.IDictService;
import com.cloudx.intf.basic.dict.dto.DictCreateRequest;
import com.cloudx.intf.basic.dict.dto.DictModifyRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典接口实现
 * @author heycm
 * @version 1.0
 * @since 2025/7/18 0:17
 */
@RestController
@RequestMapping("/dict")
public class DictServiceImpl implements IDictService {

    @Override
    public Result<?> create(DictCreateRequest request) {
        return Result.error("操作失败");
    }

    @Override
    public Result<?> modify(DictModifyRequest request) {
        return Result.error("操作失败");
    }
}
