package com.cloudx.platform.websocket.model.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息实体基类
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:16
 */
@Data
@Accessors(chain = true)
public abstract class BaseMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 3506619647000238975L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息类型
     */
    private final MessageType messageType;

    /**
     * 时间戳
     */
    private long timestamp;

    public BaseMessage() {
        this.messageType = MessageType.UNKNOWN;
    }

    public BaseMessage(MessageType messageType) {
        this.messageType = messageType;
    }
}
