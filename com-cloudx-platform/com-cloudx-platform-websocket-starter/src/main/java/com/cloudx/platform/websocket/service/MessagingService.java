package com.cloudx.platform.websocket.service;

/**
 * 消息服务
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:54
 */
public interface MessagingService {

    /**
     * [私密频道] 发送消息
     * @param sessionId   会话id
     * @param destination 地址
     * @param payload     消息
     */
    void sendTo(String sessionId, String destination, Object payload);

    /**
     * [私密频道] 发送群组消息
     * @param senderId    发送者会话id
     * @param groupId     群组id
     * @param destination 地址
     * @param payload     消息
     */
    void sendGroup(String senderId, String groupId, String destination, Object payload);

    /**
     * [公开频道] 发送主题消息，订阅频道 [/{destinationPrefix}/{topicId}] 即可收到消息
     * @param topicId           主题
     * @param destinationPrefix 地址前缀
     * @param payload           消息
     */
    void sendTopic(String topicId, String destinationPrefix, Object payload);

    /**
     * [公开频道] 发送广播消息，订阅频道 [/broadcast] 即可收到消息
     * @param payload 消息
     */
    void sendBroadcast(Object payload);

    /**
     * [公开频道] 发送广播消息，订阅频道 [/broadcast/{destinationSuffix}] 即可收到消息
     * @param destinationSuffix 地址后缀
     * @param payload           消息
     */
    void sendBroadcast(String destinationSuffix, Object payload);
}
