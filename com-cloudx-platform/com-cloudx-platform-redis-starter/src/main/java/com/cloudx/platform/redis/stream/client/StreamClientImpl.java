package com.cloudx.platform.redis.stream.client;

import com.cloudx.common.tools.IPUtil;
import com.cloudx.platform.redis.stream.consumer.ParallelConsumer;
import com.cloudx.platform.redis.stream.message.MessageHandler;
import com.cloudx.platform.redis.util.RedisCallWrapper;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
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

    private static final AtomicInteger CONSUMER_COUNT = new AtomicInteger(0);
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
    public void createIfAbsent(String streamKey, String groupName) {
        createIfAbsent(streamKey, groupName, ReadOffset.lastConsumed());
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
    public long trim(String streamKey, long maxCount) {
        return RedisCallWrapper.longCall(() -> redisTemplate.boundStreamOps(streamKey).trim(maxCount));
    }

    @Override
    public void startConsumer(ParallelConsumer consumer, MessageHandler handler) {
        for (int i = 0; i < consumer.getConcurrency(); i++) {
            // Poll 偏移量
            StreamOffset<String> streamOffset = StreamOffset.create(consumer.getStreamKey(), ReadOffset.lastConsumed());
            // 构造读请求
            StreamMessageListenerContainer.ConsumerStreamReadRequest<String> request = StreamMessageListenerContainer.StreamReadRequest
                    .builder(streamOffset)
                    .consumer(Consumer.from(consumer.getGroupName(),
                            consumer.getStreamKey() + "-" + consumer.getGroupName() + "-" + IPUtil.LOCAL_IP + "-" + CONSUMER_COUNT.incrementAndGet()))
                    .autoAcknowledge(false)
                    .cancelOnError(throwable -> false)
                    .build();
            // 消息监听，Container读到消息后通知监听器处理 => TODO 批量消费
            StreamListener<String, ObjectRecord<String, String>> listener = record -> {
                String value = record.getValue();
                EXECUTOR.execute(() -> {
                    try {
                        handler.onMessage(value);
                    } catch (Exception e) {
                        handler.onError(record, e);
                    } finally {
                        redisTemplate.opsForStream().acknowledge(consumer.getStreamKey(), record);
                    }
                });
            };
            // 注册监听器
            container.register(request, listener);
        }
    }
}
