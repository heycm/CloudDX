package com.cloudx.platform.sms.aliyun.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.sms.common.entity.SmsRequest;
import com.cloudx.platform.sms.common.entity.SmsResponse;
import com.cloudx.platform.sms.common.properties.SmsProperties;
import com.cloudx.platform.sms.common.service.SmsService;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云短信实现
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 21:50
 */
@Slf4j
public class AliyunSmsServiceImpl implements SmsService {

    private final Client client;

    public AliyunSmsServiceImpl(SmsProperties properties) throws Exception {
        // https://next.api.aliyun.com/api/Dysmsapi/2017-05-25/SendSms?tab=DEMO&lang=JAVA
        Config config = new Config().setAccessKeyId(properties.getAccessKey()).setAccessKeySecret(properties.getSecretKey());
        config.endpoint = "dysmsapi.aliyuncs.com";
        client = new com.aliyun.dysmsapi20170525.Client(config);
    }

    @Override
    public SmsResponse send(SmsRequest request) {
        log.info("SMSAliyun 发送短信: {}", Jackson.toJson(request));
        try {
            SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(String.join(",", request.getPhoneIds()))
                    .setSignName(request.getSignature()).setTemplateCode(request.getTemplateId())
                    .setTemplateParam(Jackson.toJson(request.getData()));
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody body = sendSmsResponse.body;
            if (body == null) {
                log.warn("SMSAliyun 发送失败: 响应Body为空");
                return SmsResponse.of().setSuccess(false).setMessage("响应Body为空");
            }
            SmsResponse response = SmsResponse.of().setSuccess("OK".equals(body.code)).setCode(body.code).setMessage(body.message)
                    .setRequestId(body.requestId);
            if (response.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("SMSAliyun 发送成功: {}", Jackson.toJson(response));
                }
            } else {
                log.warn("SMSAliyun 发送失败 响应报文: {}", Jackson.toJson(sendSmsResponse));
            }
            return response;
        } catch (TeaException e) {
            SmsResponse response = SmsResponse.of().setSuccess(false).setCode(e.code).setMessage(e.message).setData(e.data);
            log.warn("SMSAliyun 发送失败: {}", Jackson.toJson(response));
            return response;
        } catch (Exception e) {
            log.error("SMSAliyun 发送异常: {}", e.getMessage(), e);
            return SmsResponse.of().setSuccess(false).setMessage("发送异常");
        }
    }
}
