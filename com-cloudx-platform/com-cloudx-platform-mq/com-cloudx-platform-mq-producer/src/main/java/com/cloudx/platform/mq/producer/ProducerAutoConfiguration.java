package com.cloudx.platform.mq.producer;

import com.cloudx.platform.mq.common.handler.DefaultTransactionHandler;
import com.cloudx.platform.mq.common.handler.TransactionHandler;
import com.cloudx.platform.mq.producer.callback.DefaultSendCallback;
import com.cloudx.platform.mq.producer.service.EventService;
import com.cloudx.platform.mq.producer.service.EventServiceImpl;
import com.cloudx.platform.mq.producer.transaction.LocalTransactionListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 生产者配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/30 0:04
 */
@Configuration
@ConditionalOnProperty(value = "rocketmq.name-server")
@Slf4j
public class ProducerAutoConfiguration {

    public ProducerAutoConfiguration() {
        log.info("platform component [MQ Producer] starter ready...");
    }

    /**
     * 默认事务处理器
     * @return
     */
    @Bean
    @Primary
    public TransactionHandler transactionHandler() {
        return new DefaultTransactionHandler();
    }

    /**
     * 默认发送回调
     * @return
     */
    @Bean
    @Primary
    public SendCallback sendCallback() {
        return new DefaultSendCallback();
    }

    /**
     * 本地事务监听器
     * @return
     */
    @Bean
    public TransactionListener transactionListener() {
        return new LocalTransactionListener();
    }

    /**
     * 事件服务
     * @param rocketMQTemplate    rocketmq api
     * @param transactionListener 事务监听器
     * @return
     */
    @Bean
    public EventService eventService(RocketMQTemplate rocketMQTemplate, TransactionListener transactionListener) {
        return new EventServiceImpl(rocketMQTemplate, transactionListener);
    }
}
