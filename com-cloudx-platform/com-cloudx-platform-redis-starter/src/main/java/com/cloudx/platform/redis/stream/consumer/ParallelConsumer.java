package com.cloudx.platform.redis.stream.consumer;

import lombok.Data;

/**
 * 并行消费者组
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:25
 */
@Data
public class ParallelConsumer {

    /**
     * stream key
     */
    private String streamKey;

    /**
     * 消费者组名称
     */
    private String groupName;

    /**
     * 组内消费者数量
     */
    private int concurrency = 1;
}
