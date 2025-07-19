package com.cloudx.basic.adapter.intf.dict.assembler;

import com.cloudx.basic.application.dict.command.DictCreateCommand;
import com.cloudx.intf.basic.dict.dto.DictCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/7/19 21:52
 */
@Mapper
public interface DictAssembler {

    DictAssembler INST = Mappers.getMapper(DictAssembler.class);

    DictCreateCommand convert(DictCreateRequest request);
}
