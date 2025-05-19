package com.cloudx.platform.websocket.service;

import java.util.Set;

/**
 * 消息服务
 * @author heycm
 * @version 1.0
 * @since 2025/5/6 17:54
 */
public interface MessagingService {

    /**
     * [私密频道] 发送消息
     * @param user        目标用户
     * @param destination 地址
     * @param payload     消息
     */
    void sendToUser(String user, String destination, Object payload);

    /**
     * [私密频道] 群发消息
     * @param users       用户集合
     * @param destination 地址
     * @param payload     消息
     */
    void sendToUsers(Set<String> users, String destination, Object payload);

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
