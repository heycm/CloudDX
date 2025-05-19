package com.cloudx.platform.websocket.message;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主题消息
 * @author heycm
 * @version 1.0
 * @since 2025/5/19 22:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = -88125288269906877L;

    /**
     * 监听地址
     */
    private String destination;

    /**
     * 内容
     */
    private Object payload;
}
