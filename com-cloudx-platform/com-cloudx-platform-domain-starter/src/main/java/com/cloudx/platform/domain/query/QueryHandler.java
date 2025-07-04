package com.cloudx.platform.domain.query;

import com.cloudx.common.entity.error.Optional;

/**
 * 查询处理器
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 23:12
 */
public interface QueryHandler<T extends Query<R>, R> {

    /**
     * 执行查询
     * @param query 查询参数
     * @return 查询结果
     */
    Optional<R> handle(T query);

}
