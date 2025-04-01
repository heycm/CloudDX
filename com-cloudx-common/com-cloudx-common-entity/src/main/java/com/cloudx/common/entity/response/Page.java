package com.cloudx.common.entity.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 20:02
 */
@Data
public class Page<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3129718698143232335L;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页数量
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据列表
     */
    private List<E> list;
}
