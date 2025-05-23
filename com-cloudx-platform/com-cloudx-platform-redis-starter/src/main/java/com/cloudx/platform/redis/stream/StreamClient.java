package com.cloudx.platform.redis.stream;

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

    void createIfAbsent(String streamKey, String groupName, ReadOffset readOffset);

    void trim(String streamKey, long maxEntries);

    void startConsumer(ParallelConsumer consumer, MessageHandler handler);
}
