package com.cloudx.platform.redis.stream.message;

import org.springframework.data.redis.connection.stream.ObjectRecord;

/**
 * 消息处理
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:12
 */
// @FunctionalInterface
public interface MessageHandler {

    void onMessage(String message);

    void onError(ObjectRecord<String, String> record, Throwable throwable);
}
