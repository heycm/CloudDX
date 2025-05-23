package com.cloudx.platform.redis.stream.client;

import com.cloudx.common.tools.threadpool.virtual.VirtualThread;
import com.cloudx.platform.redis.stream.StreamClient;
import com.cloudx.platform.redis.stream.consumer.ParallelConsumer;
import com.cloudx.platform.redis.stream.message.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 16:37
 */
@Slf4j
public class StreamClientImpl implements StreamClient {

    private static final Executor EXECUTOR;

    private final RedisTemplate<String, Object> redisTemplate;
    private final StreamMessageListenerContainer<String, ObjectRecord<String, String>> container;

    static {
        EXECUTOR = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("vThread-stream-consumer-", 1).factory());
    }

    public StreamClientImpl(RedisTemplate<String, Object> redisTemplate,
                            StreamMessageListenerContainer<String, ObjectRecord<String, String>> container) {
        this.redisTemplate = redisTemplate;
        this.container = container;
    }

    @Override
    public void createIfAbsent(String streamKey, String groupName, ReadOffset readOffset) {
        try {
            redisTemplate.boundStreamOps(streamKey).createGroup(readOffset, groupName);
            log.info("Create Redis Stream: {}, Consumer group: {}", streamKey, groupName);
        } catch (RedisSystemException e) {
            if (!e.getMessage().contains("BUSYGROUP")) {
                throw e;
            }
            log.info("Redis Stream: {}, Consumer group: {} already exists", streamKey, groupName);
        }
    }

    @Override
    public void trim(String streamKey, long maxEntries) {
        redisTemplate.boundStreamOps(streamKey).trim(maxEntries);
    }

    @Override
    public void startConsumer(ParallelConsumer consumer, MessageHandler handler) {
        for (int i = 0; i < consumer.getConcurrency(); i++) {
            StreamOffset<String> streamOffset = StreamOffset.create(consumer.getStreamKey(), ReadOffset.lastConsumed());
            StreamMessageListenerContainer.ConsumerStreamReadRequest<String> request = StreamMessageListenerContainer.StreamReadRequest
                    .builder(streamOffset)
                    .consumer(Consumer.from(consumer.getGroupName(), "todo"))
                    .autoAcknowledge(consumer.isAutoAck())
                    .build();
            container.register(request, record -> {
                String value = record.getValue();
                EXECUTOR.execute(() -> {
                    try {
                        handler.onMessage(value);
                    } catch (Exception e) {
                        handler.onError(record, e);
                    }
                });
            });
        }
    }
}
