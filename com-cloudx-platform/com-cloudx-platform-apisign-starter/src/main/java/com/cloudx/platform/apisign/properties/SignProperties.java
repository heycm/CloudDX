package com.cloudx.platform.apisign.properties;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SignProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 2034143800298004701L;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 签名密钥
     */
    private String secretKey;

    /**
     * 是否开启签名验证
     */
    private boolean enable;
}
