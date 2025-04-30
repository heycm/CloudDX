package com.cloudx.platform.websocket.autoconfigure;

import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import com.cloudx.platform.websocket.core.config.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * WebSocket Server 自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 21:41
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebSocketProperties.class)
@Import({WebSocketConfig.class})
@Slf4j
public class WebSocketAutoConfiguration {

    public WebSocketAutoConfiguration() {
        log.info("platform component [WebSocket Server] starter ready...");
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketAuthenticator webSocketAuthenticator() {
        return (request, response) -> true;
    }
}
