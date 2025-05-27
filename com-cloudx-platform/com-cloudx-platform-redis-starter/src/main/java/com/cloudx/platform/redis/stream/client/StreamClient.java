package com.cloudx.platform.redis.stream.client;

import com.cloudx.platform.redis.stream.consumer.ParallelConsumer;
import com.cloudx.platform.redis.stream.message.MessageHandler;
import org.springframework.data.redis.connection.stream.ReadOffset;

/**
 * 流消息
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 16:31
 */
public interface StreamClient {

    /**
     * 创建流
     * @param streamKey 流名称
     * @param groupName 消费者组名称
     */
    void createIfAbsent(String streamKey, String groupName);

    /**
     * 创建流
     * @param streamKey  流名称
     * @param groupName  消费者组名称
     * @param readOffset 读取偏移量
     */
    void createIfAbsent(String streamKey, String groupName, ReadOffset readOffset);

    /**
     * 截断流，保留最大条数
     * @param streamKey 流名称
     * @param maxCount  最大容量
     */
    long trim(String streamKey, long maxCount);

    /**
     * 启动消费监听
     * @param consumer 消费者
     * @param handler  消息处理
     */
    void startConsumer(ParallelConsumer consumer, MessageHandler handler);
}
