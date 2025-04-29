package com.cloudx.platform.websocket.model.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息实体基类
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:16
 */
@Setter
@Getter
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
    private String messageType;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 当前会话ID
     */
    private String currentSessionId;
}
