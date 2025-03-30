package cn.heycm.platform.mq.producer.service;

import cn.heycm.platform.mq.common.event.Event;
import org.apache.rocketmq.client.producer.SendCallback;

/**
 * 事件消息服务
 * @author heycm
 * @version 1.0
 * @since 2025/3/30 0:06
 */
public interface EventService {

    /**
     * 推送普通事件
     * @param event 事件
     * @return 是否成功
     */
    boolean push(Event event);

    /**
     * 推送事务事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushInTransaction(Event event);

    /**
     * 推送广播事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushBroadcast(Event event);

    /**
     * 推送延迟事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushDelay(Event event);

    /**
     * 推送有序事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushOrderly(Event event);

    /**
     * 异步推送事件
     * @param event    事件
     * @param callback 回调
     */
    void asyncPush(Event event, SendCallback callback);

    /**
     * 异步推送延迟事件
     * @param event    事件
     * @param callback 回调
     */
    void asyncPushDelay(Event event, SendCallback callback);

    /**
     * 异步推送顺序事件
     * @param event    事件
     * @param callback 回调
     */
    void asyncPushOrderly(Event event, SendCallback callback);

}
