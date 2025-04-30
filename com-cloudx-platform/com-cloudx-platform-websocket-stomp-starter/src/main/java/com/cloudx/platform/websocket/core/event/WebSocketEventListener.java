package com.cloudx.platform.websocket.core.event;

import com.cloudx.platform.websocket.routing.ChannelGroupRegistry;
import com.cloudx.platform.websocket.routing.DynamicChannelRegistry;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * 事件监听
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 22:54
 */
public class WebSocketEventListener {

    private final DynamicChannelRegistry dynamicChannelRegistry;
    private final ChannelGroupRegistry channelGroupRegistry;

    public WebSocketEventListener(DynamicChannelRegistry dynamicChannelRegistry, ChannelGroupRegistry channelGroupRegistry) {
        this.dynamicChannelRegistry = dynamicChannelRegistry;
        this.channelGroupRegistry = channelGroupRegistry;
    }

    /**
     * 监听会话断开事件
     * @param event
     */
    public void onSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        dynamicChannelRegistry.removeChannel(sessionId);
        channelGroupRegistry.clearChannel(sessionId);
    }
}
