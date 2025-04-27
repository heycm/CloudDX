package com.cloudx.platform.websocket.model.message;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 普通文本消息
 * @author heycm
 * @version 1.0
 * @since 2025/4/27 17:31
 */
@Data
@Accessors(chain = true)
public class TextPlainMessage extends BaseMessage {

    /**
     * 消息内容
     */
    private String text;

    public TextPlainMessage() {
        super(MessageType.TEXT_PLAIN);
    }
}
