package com.cloudx.platform.websocket.decorator;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * 消息压缩增强
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 21:16
 */
public class CompressionDecorator extends WebSocketHandlerDecorator {

    /**
     * 默认压缩阈值：byte 1M
     */
    public static final int DEFAULT_COMPRESSION_THRESHOLD = 1024 * 1024;

    /**
     * 默认压缩级别
     */
    public static final int DEFAULT_COMPRESSION_LEVEL = 6;

    public CompressionDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() >= DEFAULT_COMPRESSION_THRESHOLD) {
            message = compress(message);
        }
        super.handleMessage(session, message);
    }

    /**
     * 压缩消息
     * @param message
     * @return
     */
    private WebSocketMessage<?> compress(WebSocketMessage<?> message) {
        return message;
    }
}
