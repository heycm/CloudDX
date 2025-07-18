package com.cloudx.basic.adapter.intf.dict;

import com.cloudx.basic.adapter.intf.dict.assembler.DictAssembler;
import com.cloudx.basic.application.dict.command.DictCreateCommand;
import com.cloudx.common.entity.error.Optional;
import com.cloudx.common.entity.response.Result;
import com.cloudx.intf.basic.dict.IDictService;
import com.cloudx.intf.basic.dict.dto.DictCreateRequest;
import com.cloudx.intf.basic.dict.dto.DictModifyRequest;
import com.cloudx.platform.domain.command.CommandBus;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DictServiceImpl implements IDictService {

    private final CommandBus commandBus;

    @Override
    public Result<?> create(DictCreateRequest request) {
        DictCreateCommand command = DictAssembler.INST.convert(request);
        Optional<Void> dispatch = commandBus.dispatch(command);
        return Result.optional(dispatch);
    }

    @Override
    public Result<?> modify(DictModifyRequest request) {
        return Result.error("操作失败");
    }
}
