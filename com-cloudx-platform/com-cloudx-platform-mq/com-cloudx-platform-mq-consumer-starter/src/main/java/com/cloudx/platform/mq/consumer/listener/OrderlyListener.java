package com.cloudx.platform.mq.consumer.listener;

import com.cloudx.platform.mq.common.constant.Constant;
import com.cloudx.platform.mq.common.event.Event;
import com.cloudx.platform.mq.consumer.multicaster.EventMulticaster;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * 顺序消息监听
 * @author heycm
 * @version 1.0
 * @since 2025/4/1 21:21
 */
@RocketMQMessageListener(topic = "${spring.application.name}" + Constant.ORDERLY_SUFFIX, consumerGroup = "${spring.application.name}"
        + Constant.ORDERLY_SUFFIX, consumeMode = ConsumeMode.ORDERLY)
public class OrderlyListener implements RocketMQListener<Event> {

    @Override
    public void onMessage(Event event) {
        EventMulticaster.onEvent(event);
    }
}
