package com.cloudx.platform.redis.stream.consumer;

import com.cloudx.platform.redis.stream.message.DefaultRetryPolicy;
import com.cloudx.platform.redis.stream.message.RetryPolicy;
import lombok.Data;

/**
 * 并行消费者组
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:25
 */
@Data
public class ParallelConsumer {

    private String streamKey;

    private String groupName;

    private int concurrency;

    private int batchSize = 50;

    private long pollTimeout = 300;

    private boolean autoAck = true;

    private RetryPolicy retryPolicy = new DefaultRetryPolicy(3);
}
