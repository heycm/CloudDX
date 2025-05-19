package com.cloudx.platform.websocket.message;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群发消息
 * @author heycm
 * @version 1.0
 * @since 2025/5/19 22:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MassMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = -7886663029266419483L;

    /**
     * 群发用户
     */
    private Set<String> users;

    /**
     * 监听地址
     */
    private String destination;

    /**
     * 内容
     */
    private Object payload;
}
