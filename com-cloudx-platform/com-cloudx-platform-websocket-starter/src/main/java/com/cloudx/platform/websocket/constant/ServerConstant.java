package com.cloudx.platform.websocket.constant;

/**
 * WebSocket Server 常量
 * @author heycm
 * @version 1.0
 * @since 2025/5/14 18:17
 */
public interface ServerConstant {

    /**
     * 用户会话标识
     */
    String ATTRIBUTE_USER_KEY = "CLOUDX_USER_ID";

    /**
     * 会话缓存key前缀：websocket:session:{userId} -> session
     */
    String SESSION_KEY_PREFIX = "websocket:session:";

    /**
     * 群组缓存key前缀：websocket:group:{groupId} -> [user1,user2]
     */
    String GROUP_KEY_PREFIX = "websocket:group:";

    /**
     * 会话加入群组缓存key前缀：websocket:group:session:join:{user} -> [groupId1,groupId2]
     */
    String GROUP_JOIN_PREFIX = "websocket:group:session:join:";

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
