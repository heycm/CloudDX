package com.cloudx.platform.websocket.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSocket 配置项
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 21:26
 */
@Data
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {

    /**
     * websocket endpoint
     */
    private String endpoint = "/ws";

    /**
     * 客户端订阅地址前缀
     **/
    private String[] clientDestPrefixs = {"/topic", "/broadcast", "/user"};

    /**
     * 客户端订阅地址前缀（当前连接用户私人频道）
     */
    private String userDestPrefix = "/user";

    /**
     * 服务端消息处理地址前缀
     */
    private String[] appDestPrefixs = {"/app"};

    /**
     * 是否启用认证
     */
    private boolean authEnabled;

    /**
     * 允许跨域访问的域名
     */
    private String[] allowedOrigins = {"*"};

    /**
     * 发送消息超时时间(ms)
     */
    private int sendTimeLimit = 5000;

    /**
     * 发送缓存大小限制(byte)
     */
    private int sendBufferSizeLimit = 1024 * 1024;

    /**
     * 心跳检查配置
     */
    private Heartbeat heartbeat = new Heartbeat();

    /**
     * 消息中转方式
     */
    private BrokerRelay brokerRelay = BrokerRelay.Local;

    /**
     * 心跳检查配置
     */
    @Data
    public static class Heartbeat {

        /**
         * 心跳检查间隔(ms)
         */
        private long checkInterval = 30000;

        /**
         * 最大允许间隔(ms)
         */
        private long maxInterval = 120000;

        /**
         * 最大允许重试次数
         */
        private int maxRetries = 3;
    }

    /**
     * 消息中转方式
     */
    public enum BrokerRelay {

        /**
         * 本地消息中转
         */
        Local,

        /**
         * Redis 消息中转（Redis不支持Stomp协议，实现方式为发布订阅机制）
         */
        Redis,

        /**
         * RabbitMQ 消息中转（RabbitMQ支持Stomp协议）
         */
        RabbitMQ,

        /**
         * ActiveMQ 消息中转（ActiveMQ支持Stomp协议）
         */
        ActiveMQ;
    }
}
