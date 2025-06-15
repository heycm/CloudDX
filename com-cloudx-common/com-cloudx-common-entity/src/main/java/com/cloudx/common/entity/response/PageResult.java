package com.cloudx.common.entity.response;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页统一响应模型
 * @author heycm
 * @version 1.0
 * @since 2025/6/15 20:40
 */
public class PageResult<E> extends Result<Page<E>> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7341370579715515631L;
}
