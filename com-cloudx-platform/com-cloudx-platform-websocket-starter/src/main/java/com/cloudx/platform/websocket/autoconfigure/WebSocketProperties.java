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
     * Redis 消息中转配置
     */
    private RedisProperties redis = new RedisProperties();

    /**
     * 心跳检查配置
     */
    private Heartbeat heartbeat = new Heartbeat();

    /**
     * Redis 配置项
     */
    @Data
    public static class RedisProperties {

        /**
         * Redis地址
         */
        private String host = "localhost";

        /**
         * Redis端口
         */
        private int port = 6379;

        /**
         * Redis密码
         */
        private String password;

        /**
         * Redis数据库索引
         */
        private int database = 0;
    }

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
}
