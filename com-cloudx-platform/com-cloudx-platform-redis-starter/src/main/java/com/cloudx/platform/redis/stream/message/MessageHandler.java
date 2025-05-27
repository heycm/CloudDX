package com.cloudx.platform.redis.stream.message;

import org.springframework.data.redis.connection.stream.ObjectRecord;

/**
 * 消息处理
 * @author heycm
 * @version 1.0
 * @since 2025/5/23 17:12
 */
public interface MessageHandler {

    /**
     * 处理消息
     * @param message 消息
     */
    void onMessage(String message);

    /**
     * 处理错误
     * @param record    消息
     * @param throwable 错误
     */
    void onError(ObjectRecord<String, String> record, Throwable throwable);
}
