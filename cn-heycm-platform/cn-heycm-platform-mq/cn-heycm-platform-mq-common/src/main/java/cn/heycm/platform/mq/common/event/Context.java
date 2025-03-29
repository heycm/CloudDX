package cn.heycm.platform.mq.common.event;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 事件上下文
 * @author heycm
 * @version 1.0
 * @since 2025/3/29 23:12
 */
@Data
public class Context implements Serializable {

    @Serial
    private static final long serialVersionUID = 6196941919562669589L;

    /**
     * 数据
     */
    private String data;

    public static Context of(String data) {
        Context context = new Context();
        context.setData(data);
        return context;
    }
}
