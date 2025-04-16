package com.cloudx.platform.sms.common.service;

import com.cloudx.platform.sms.common.entity.SmsRequest;
import com.cloudx.platform.sms.common.entity.SmsResponse;

/**
 * 短信服务
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 21:43
 */
public interface SmsService {

    /**
     * 发送短信
     * @param request 短信参数
     * @return 响应结果
     */
    SmsResponse send(SmsRequest request);
}
