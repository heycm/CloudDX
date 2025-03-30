package cn.heycm.platform.mq.producer.service;

import cn.heycm.platform.mq.common.constant.Constant;
import cn.heycm.platform.mq.common.event.Event;
import cn.heycm.platform.mq.producer.transaction.TransactionHandlerCache;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

/**
 * 消息推送服务实现
 * @author heycm
 * @version 1.0
 * @since 2025/3/30 23:18
 */
@Slf4j
public class EventServiceImpl implements EventService {


    private final RocketMQTemplate rocketMQTemplate;

    public EventServiceImpl(RocketMQTemplate rocketMQTemplate, TransactionListener transactionListener) {
        this.rocketMQTemplate = rocketMQTemplate;
        TransactionMQProducer producer = (TransactionMQProducer) rocketMQTemplate.getProducer();
        producer.setTransactionListener(transactionListener);
    }

    @Override
    public boolean push(Event event) {
        String topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        SendResult result = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(event).build(), event.getTimeout());
        return success(result);
    }

    @Override
    public boolean pushInTransaction(Event event) {
        TransactionHandlerCache.register(event);
        String topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(topic, MessageBuilder.withPayload(event).build(), "");
        return success(result);
    }

    @Override
    public boolean pushBroadcast(Event event) {
        TransactionHandlerCache.register(event);
        String topic = event.getTopic() + Constant.BROADCAST_SUFFIX;
        topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction(topic, MessageBuilder.withPayload(event).build(), "");
        return success(result);
    }

    @Override
    public boolean pushDelay(Event event) {
        String topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        SendResult result = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(event).build(), event.getTimeout(),
                event.getDelay());
        return success(result);
    }

    @Override
    public boolean pushOrderly(Event event) {
        String topic = event.getTopic() + Constant.ORDERLY_SUFFIX;
        topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        SendResult result = rocketMQTemplate.syncSendOrderly(topic, MessageBuilder.withPayload(event).build(), event.getHash(),
                event.getTimeout());
        return success(result);
    }

    @Override
    public void asyncPush(Event event, SendCallback callback) {
        String topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(event).build(), callback, event.getTimeout());
    }

    @Override
    public void asyncPushDelay(Event event, SendCallback callback) {
        String topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(event).build(), callback, event.getTimeout(), event.getDelay());
    }

    @Override
    public void asyncPushOrderly(Event event, SendCallback callback) {
        String topic = event.getTopic() + Constant.ORDERLY_SUFFIX;
        topic = StringUtils.hasText(event.getTags()) ? event.getTopic() + ":" + event.getTags() : event.getTopic();
        rocketMQTemplate.asyncSendOrderly(topic, MessageBuilder.withPayload(event).build(), event.getHash(), callback, event.getTimeout());
    }

    private static boolean push(Supplier<SendResult> supplier) {
        try {
            SendResult result = supplier.get();
            boolean success = success(result);
            if (success) {
                log.info("{}", result);
            } else {
                log.error("{}", result);
            }
            return success;
        } catch (Exception e) {
            log.error("消息推送异常: {}", e.getMessage(), e);
        }
        return false;
    }

    private static boolean success(SendResult result) {
        if (result instanceof TransactionSendResult tr) {
            return SendStatus.SEND_OK == tr.getSendStatus() && LocalTransactionState.COMMIT_MESSAGE == tr.getLocalTransactionState();
        }
        return SendStatus.SEND_OK == result.getSendStatus();
    }
}
