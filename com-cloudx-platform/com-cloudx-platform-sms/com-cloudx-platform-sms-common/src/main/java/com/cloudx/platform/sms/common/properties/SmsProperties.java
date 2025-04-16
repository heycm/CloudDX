package com.cloudx.platform.sms.common.properties;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 短信配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 21:27
 */
@Data
public class SmsProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 8422946381105309349L;

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Secret Key
     */
    private String secretKey;
}
