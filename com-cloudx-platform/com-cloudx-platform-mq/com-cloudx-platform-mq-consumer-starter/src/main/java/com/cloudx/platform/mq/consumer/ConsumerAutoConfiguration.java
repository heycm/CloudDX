package com.cloudx.platform.mq.consumer;

import com.cloudx.platform.mq.consumer.annotation.EventHandler;
import com.cloudx.platform.mq.consumer.listener.BroadcastListener;
import com.cloudx.platform.mq.consumer.listener.ClusterListener;
import com.cloudx.platform.mq.consumer.listener.OrderlyListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息消费配置
 * @author heycm
 * @version 1.0
 * @since 2025/04/01 17:40
 */
@Configuration
@ConditionalOnProperty(value = "rocketmq.name-server")
@Slf4j
public class ConsumerAutoConfiguration {

    public ConsumerAutoConfiguration() {
        log.info("platform component [MQ Consumer] starter ready...");
    }

    @Bean(name = "eventHandlers")
    public List<Object> eventHandlers(ApplicationContext applicationContext) {
        String[] names = applicationContext.getBeanNamesForAnnotation(EventHandler.class);
        List<Object> handlers = new ArrayList<>(names.length);
        for (String name : names) {
            handlers.add(applicationContext.getBean(name));
        }
        return handlers;
    }

    /**
     * 集群消费监听器，保证 EventMulticaster 已经初始化
     */
    @Bean
    @ConditionalOnBean(name = "eventHandlers")
    public ClusterListener clusterListener() {
        return new ClusterListener();
    }

    /**
     * 广播消费监听器，保证 EventMulticaster 已经初始化
     */
    @Bean
    @ConditionalOnBean(name = "eventHandlers")
    public BroadcastListener broadcastListener() {
        return new BroadcastListener();
    }

    /**
     * 有序消费监听器，保证 EventMulticaster 已经初始化
     */
    @Bean
    @ConditionalOnBean(name = "eventHandlers")
    public OrderlyListener orderlyListener() {
        return new OrderlyListener();
    }
}
