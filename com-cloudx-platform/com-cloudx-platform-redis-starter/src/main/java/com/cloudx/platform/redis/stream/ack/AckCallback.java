package com.cloudx.platform.redis.stream.ack;

/**
 * ACK回调
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:11
 */
@FunctionalInterface
public interface AckCallback {

    void acknowledge();

}
