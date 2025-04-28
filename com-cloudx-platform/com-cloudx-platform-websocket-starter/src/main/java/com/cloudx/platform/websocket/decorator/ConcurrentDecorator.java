package com.cloudx.platform.websocket.decorator;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * 并发
 * @author heycm
 * @version 1.0
 * @since 2025/4/28 11:10
 */
public class ConcurrentDecorator extends WebSocketHandlerDecorator {

    // 发送超时时间：单次消息发送允许的最大时间（毫秒），超时则抛出异常。
    private final int sendTimeLimit = 10 * 1000;
    // 缓冲区大小限制：缓冲区的最大容量（字节），超出则触发异常。
    private final int bufferSizeLimit = 1024 * 1024;
    private final ConcurrentWebSocketSessionDecorator.OverflowStrategy overflowStrategy = ConcurrentWebSocketSessionDecorator.OverflowStrategy.TERMINATE;

    public ConcurrentDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session = new ConcurrentWebSocketSessionDecorator(session, sendTimeLimit, bufferSizeLimit, overflowStrategy);
        super.afterConnectionEstablished(session);
    }
}
