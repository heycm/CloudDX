package com.cloudx.platform.websocket.autoconfigure;

import com.cloudx.platform.websocket.core.auth.AuthResult;
import com.cloudx.platform.websocket.core.auth.WebSocketAuthenticator;
import com.cloudx.platform.websocket.core.config.WebSocketConfig;
import com.cloudx.platform.websocket.core.heartbeat.HeartbeatController;
import com.cloudx.platform.websocket.core.heartbeat.HeartbeatMonitor;
import com.cloudx.platform.websocket.repository.GroupRepository;
import com.cloudx.platform.websocket.repository.SessionRepository;
import com.cloudx.platform.websocket.repository.impl.RedisGroupRepository;
import com.cloudx.platform.websocket.repository.impl.RedisSessionReposiyory;
import com.cloudx.platform.websocket.service.MessagingService;
import com.cloudx.platform.websocket.service.MessagingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * WebSocket Server 自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 21:41
 */
@Configuration
// @ConditionalOnWebApplication
@ConditionalOnBean(RedisTemplate.class)
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
        return (request) -> {
            AuthResult result = new AuthResult();
            result.setSuccess(true);
            result.setUserId("guest");
            return result;
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupRepository groupRepository(RedisTemplate<String, Object> redisTemplate) {
        return new RedisGroupRepository(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionRepository sessionRepository(RedisTemplate<String, Object> redisTemplate, GroupRepository groupRepository) {
        return new RedisSessionReposiyory(redisTemplate, groupRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public MessagingService messagingService(GroupRepository groupRepository, SimpMessagingTemplate simpMessagingTemplate) {
        return new MessagingServiceImpl(groupRepository, simpMessagingTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public HeartbeatController heartbeatController(SessionRepository sessionRepository) {
        return new HeartbeatController(sessionRepository);
    }

    // @Bean
    public HeartbeatMonitor heartbeatMonitor(WebSocketProperties webSocketProperties, RedisTemplate<String, Object> redisTemplate,
            SimpMessagingTemplate simpMessagingTemplate) {
        return new HeartbeatMonitor(webSocketProperties.getHeartbeat(), redisTemplate, simpMessagingTemplate);
    }
}
