package com.cloudx.platform.mq.consumer.listener;

import com.cloudx.platform.mq.common.event.Event;
import com.cloudx.platform.mq.consumer.multicaster.EventMulticaster;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;

/**
 * 普通消息监听
 * @author heycm
 * @version 1.0
 * @since 2025/4/1 21:11
 */
@RocketMQMessageListener(topic = "${spring.application.name}", consumerGroup = "${spring.application.name}")
public class ClusterListener implements RocketMQListener<Event>, RocketMQPushConsumerLifecycleListener {

    // 设置每次消息拉取的时间间隔 单位毫秒
    @Value("${rocketmq.consumer.pullInterval:0}")
    private int pullInterval;

    // 最小消费线程池数
    @Value("${rocketmq.consumer.consumeThreadMin:0}")
    private int consumeThreadMin;

    // 最大消费线程池数
    @Value("${rocketmq.consumer.consumeThreadMax:0}")
    private int consumeThreadMax;

    // 设置消费者单次批量消费的消息数目上限 默认1
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize:0}")
    private int consumeMessageBatchMaxSize;

    // 设置每个队列每次拉取的最大消费数
    @Value("${rocketmq.consumer.pullBatchSize:0}")
    private int pullBatchSize;

    /**
     * 消费消息
     * @param event
     */
    @Override
    public void onMessage(Event event) {
        EventMulticaster.onEvent(event);
    }

    /**
     * Push 模式消费者的生命周期事件，在启动消费实例前的回调方法，可以用来设置一些消费实例的参数
     * @param defaultMQPushConsumer
     */
    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        if (pullInterval > 0) {
            defaultMQPushConsumer.setPullInterval(pullInterval);
        }
        if (consumeThreadMin > 0) {
            defaultMQPushConsumer.setConsumeThreadMin(consumeThreadMin);
        }
        if (consumeThreadMax > 0) {
            defaultMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
        }
        if (consumeMessageBatchMaxSize > 0) {
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        }
        if (pullBatchSize > 0) {
            defaultMQPushConsumer.setPullBatchSize(pullBatchSize);
        }
    }
}
