package cn.heycm.platform.mq.producer.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * 默认异步消息推送回调
 * @author heycm
 * @version 1.0
 * @since 2025/3/30 23:19
 */
@Slf4j
public class DefaultSendCallback implements SendCallback {

    @Override
    public void onSuccess(SendResult sendResult) {
        if (log.isDebugEnabled()) {
            log.debug("异步消息推送成功: {}", sendResult);
        }
    }

    @Override
    public void onException(Throwable throwable) {
        log.error("异步消息推送异常: {}", throwable.getMessage(), throwable);
    }
}
