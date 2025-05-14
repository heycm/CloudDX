package com.cloudx.platform.websocket.constant;

/**
 * WebSocket Server 常量
 * @author heycm
 * @version 1.0
 * @since 2025/5/14 18:17
 */
public interface ServerConstant {

    /**
     * 会话缓存key前缀：websocket:session:{sessionId} -> session
     */
    String SESSION_KEY_PREFIX = "websocket:session:";

    /**
     * 用户缓存key前缀：websocket:user:{userId} -> sessionId
     */
    String USER_KEY_PREFIX = "websocket:user:";

    /**
     * 服务端主动ping客户端地址
     */
    String CLIENT_HEARTBEAT_DESTINATION = "/user/heartbeat";

    /**
     * 客户端发送心跳到服务端地址
     */
    String SERVER_HEARTBEAT_DESTINATION = "/app/heartbeat";

    /**
     * 广播地址
     */
    String BROADCAST_DESTINATION = "/broadcast";
}
