package cn.heycm.platform.mq.producer.transaction;

import cn.heycm.common.entity.error.Assert;
import cn.heycm.platform.mq.common.event.Event;
import cn.heycm.platform.mq.common.handler.TransactionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地事务处理器缓存：保存消息与事务处理的关系
 * @author heycm
 * @version 1.0
 * @since 2025/3/30 23:29
 */
public class TransactionHandlerCache {

    private static final Map<String, TransactionHandler> HANDLER = new ConcurrentHashMap<>();

    public static void register(Event event) {
        Assert.notNull(event.getHandler(), "TransactionHandler is not allowed be null.");
        HANDLER.putIfAbsent(getKey(event), event.getHandler());
    }

    public static TransactionHandler get(Event event) {
        return HANDLER.get(getKey(event));
    }

    private static String getKey(Event event) {
        return event.getTopic() + "." + event.getEvent();
    }
}
