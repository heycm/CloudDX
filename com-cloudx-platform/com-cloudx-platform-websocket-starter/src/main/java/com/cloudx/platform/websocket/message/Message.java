package com.cloudx.platform.websocket.message;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础消息
 * @author heycm
 * @version 1.0
 * @since 2025/5/19 22:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = -3458960723410544441L;

    /**
     * 用户
     */
    private String user;

    /**
     * 监听地址
     */
    private String destination;

    /**
     * 内容
     */
    private Object payload;
}
