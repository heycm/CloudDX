package com.cloudx.platform.websocket.core;

import com.cloudx.platform.websocket.model.message.BaseMessage;
import com.cloudx.platform.websocket.model.message.MessageType;

/**
 * 消息转换器
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 23:06
 */
public interface MessageConverter<T extends BaseMessage> {

    /**
     * 支持的消息类型
     * @return MessageType
     */
    String getSupportedMessageType();

    /**
     * 序列化，将消息对象转为下行数据
     * @param message 消息
     * @return 序列化后的消息
     */
    String serialize(T message);

    /**
     * 反序列化，上行数据转为对应类型消息对象
     * @param message 消息
     * @return 反序列化后的消息
     */
    T deserialize(String message);

}
