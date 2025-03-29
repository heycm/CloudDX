package cn.heycm.platform.mq.common.handler;

import cn.heycm.platform.mq.common.constant.TransactionStatus;
import cn.heycm.platform.mq.common.event.Event;

/**
 * MQ事务消息：本地事务处理器
 * @author heycm
 * @version 1.0
 * @since 2025/3/29 23:00
 */
public interface TransactionHandler {

    /**
     * 发送Half消息后执行的本地事务
     * @param event 事件
     * @return 本地事务状态
     */
    TransactionStatus execute(Event event);

    /**
     * Broker没收到本地事务最终状态时，主动反查本地事务状态，以决定是否推送Half消息
     * @param event 事件
     * @return 本地事务状态
     */
    TransactionStatus check(Event event);

}
