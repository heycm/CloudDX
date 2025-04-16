package com.cloudx.platform.oss.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * 存储桶
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:19
 */
@Data
@Builder
public class Bucket implements Serializable {

    @Serial
    private static final long serialVersionUID = -860971333973648628L;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date creationDate;
}
