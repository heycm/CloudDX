package com.cloudx.common.entity.shortid;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 号段模式-短ID
 * @author heycm
 * @version 1.0
 * @since 2025/6/18 22:48
 */
@Data
public class ShortId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2440849969017139049L;

    /**
     * ID键，区分业务，不同业务使用不同的ID键，可以产生重复的ID
     */
    private String idKey;

    /**
     * 下一个ID
     */
    private int nextId;

    /**
     * 本段ID最大值
     */
    private int maxId;
}
