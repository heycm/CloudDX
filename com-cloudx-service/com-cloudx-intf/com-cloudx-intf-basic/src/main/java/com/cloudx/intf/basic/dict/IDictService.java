package com.cloudx.intf.basic.dict;

import com.cloudx.common.entity.response.Result;
import com.cloudx.intf.basic.dict.dto.DictCreateRequest;
import com.cloudx.intf.basic.dict.dto.DictModifyRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 字典服务接口
 * @author heycm
 * @version 1.0
 * @since 2025/7/17 23:57
 */
public interface IDictService {

    /**
     * 创建字典
     * @param request
     * @return
     */
    @PostMapping("/create")
    Result<?> create(@RequestBody DictCreateRequest request);

    /**
     * 修改字典
     * @param request
     * @return
     */
    @PostMapping("/modify")
    Result<?> modify(@RequestBody DictModifyRequest request);

}
