package com.cloudx.im.config;

import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/5/16 11:46
 */
@Configuration
public class IMAppConfiguration {


    @Bean
    public WebSocketAuthenticator webSocketAuthenticator() {
        return new CloudXWebSocketAuthenticator();
    }
}
