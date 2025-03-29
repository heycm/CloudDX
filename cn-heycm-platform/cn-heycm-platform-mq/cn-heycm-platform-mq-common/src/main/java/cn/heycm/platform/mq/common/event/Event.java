package cn.heycm.platform.mq.common.event;

import cn.heycm.common.entity.constant.AppConstant;
import cn.heycm.common.entity.tenant.TenantContextHolder;
import cn.heycm.platform.mq.common.handler.TransactionHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.slf4j.MDC;

/**
 * 消息事件
 * @author heycm
 * @version 1.0
 * @since 2025/3/29 23:15
 */
@Data
public class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = -6533525692674637821L;

    /**
     * 事件ID/TraceID
     */
    private String eventId;

    /**
     * 事件UID/TraceUID
     */
    private String eventUid;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 事件主题
     */
    private String topic;

    /**
     * 事件名称
     */
    private String event;

    /**
     * 事件上下文
     */
    private Context context;

    /**
     * 发送超时，默认3000ms
     */
    private long timeout = 3000;

    /**
     * 延迟消息等级
     */
    private int delay;

    /**
     * 顺序消息HashKey
     */
    private String hash;

    /**
     * 事务消息本地事务处理器
     */
    @JsonIgnore
    private TransactionHandler handler;

    public Event() {
        this.eventId = MDC.get(AppConstant.TRACE_ID);
        this.eventUid = MDC.get(AppConstant.UID);
        this.tenantId = TenantContextHolder.getTenantId();
    }

    public static Event of(String topic, String event) {
        Event e = new Event();
        e.setTopic(topic);
        e.setEvent(event);
        return e;
    }

    public static Event of(String topic, String event, String data) {
        Event e = Event.of(topic, event);
        e.setContext(Context.of(data));
        return e;
    }

    public static Event of(EventDef event) {
        return Event.of(event.getTopic(), event.getEvent());
    }

    public static Event of(EventDef event, String data) {
        return Event.of(event.getTopic(), event.getEvent(), data);
    }

    public static Event of(EventDef event, String data, TransactionHandler handler) {
        return Event.of(event.getTopic(), event.getEvent(), data).handler(handler);
    }

    public Event data(String data) {
        this.context = Context.of(data);
        return this;
    }

    public Event context(Context context) {
        this.context = context;
        return this;
    }

    public Event delay(int delay) {
        this.delay = delay;
        return this;
    }

    public Event hash(String hash) {
        this.hash = hash;
        return this;
    }

    public Event timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Event handler(TransactionHandler handler) {
        this.handler = handler;
        return this;
    }
}
