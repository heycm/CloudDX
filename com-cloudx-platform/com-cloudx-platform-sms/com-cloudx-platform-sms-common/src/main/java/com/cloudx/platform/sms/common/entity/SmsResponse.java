package com.cloudx.platform.sms.common.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送短信响应
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 21:41
 */
@Data
@Accessors(chain = true)
public class SmsResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5129168459990846168L;

    /**
     * 短信发送结果：true-成功
     */
    private boolean success;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 请求ID
     */
    private String requestId;

    public static SmsResponse of() {
        return new SmsResponse();
    }
}
