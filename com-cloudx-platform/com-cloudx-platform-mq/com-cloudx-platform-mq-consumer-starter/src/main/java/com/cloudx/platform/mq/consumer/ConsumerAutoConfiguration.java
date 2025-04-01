package com.cloudx.platform.mq.consumer;

import com.cloudx.platform.mq.consumer.annotation.EventHandler;
import lombok.extern.slf4j.Slf4j;
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

    @Bean
    public List<Object> eventHandlers(ApplicationContext applicationContext) {
        String[] names = applicationContext.getBeanNamesForAnnotation(EventHandler.class);
        List<Object> handlers = new ArrayList<>(names.length);
        for (String name : names) {
            handlers.add(applicationContext.getBean(name));
        }
        return handlers;
    }
}
