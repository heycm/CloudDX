package com.cloudx.platform.websocket.exception;

/**
 * 发送消息异常
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 22:16
 */
public class SendMessageException extends RuntimeException {

    public SendMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
