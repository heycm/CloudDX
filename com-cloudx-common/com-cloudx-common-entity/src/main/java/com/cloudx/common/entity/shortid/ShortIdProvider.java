package com.cloudx.common.entity.shortid;

import com.cloudx.common.entity.response.Result;

/**
 * 短id生成接口
 * @author heycm
 * @version 1.0
 * @since 2025/6/18 22:56
 */
public interface ShortIdProvider {

    /**
     * 获取下一段短id
     * @param idKey 短id标识
     * @return 短id
     */
    Result<ShortId> getNextId(String idKey);

}
