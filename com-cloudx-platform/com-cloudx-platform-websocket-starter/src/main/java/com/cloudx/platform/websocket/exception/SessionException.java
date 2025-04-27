package com.cloudx.platform.websocket.exception;

/**
 * Session 异常，抛出此异常时服务端主动断连，并清除缓存
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 22:13
 */
public class SessionException extends RuntimeException {

    public SessionException(String message) {
        super(message);
    }

}
