package com.cloudx.platform.sms.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送短信请求
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 21:35
 */
@Data
@Accessors(chain = true)
public class SmsRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7455316180592638118L;

    /**
     * 发送手机
     */
    private Set<String> phoneIds = new HashSet<>();

    /**
     * 短信签名
     */
    private String signature;

    /**
     * 短信模板
     */
    private String templateId;

    /**
     * 模板变量
     */
    private Map<String, String> data = new HashMap<>();

    public static SmsRequest of() {
        return new SmsRequest();
    }

    public SmsRequest addPhoneId(String phoneId) {
        this.phoneIds.add(phoneId);
        return this;
    }

    public SmsRequest setPhoneIds(Set<String> phoneIds) {
        this.phoneIds.addAll(phoneIds);
        return this;
    }

    public SmsRequest addData(String key, Object value) {
        this.data.put(key, value.toString());
        return this;
    }

    public SmsRequest setData(Map<String, String> data) {
        if (data != null) {
            this.data.putAll(data);
        }
        return this;
    }
}
