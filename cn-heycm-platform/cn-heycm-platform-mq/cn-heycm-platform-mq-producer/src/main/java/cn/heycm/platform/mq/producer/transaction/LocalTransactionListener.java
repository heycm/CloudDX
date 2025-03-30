package cn.heycm.platform.mq.producer.transaction;

import cn.heycm.common.tools.Jackson;
import cn.heycm.platform.mq.common.constant.TransactionStatus;
import cn.heycm.platform.mq.common.event.Event;
import cn.heycm.platform.mq.common.handler.TransactionHandler;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 本地事务监听
 * @author heycm
 * @version 1.0
 * @since 2025/3/30 23:26
 */
public class LocalTransactionListener implements TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        Event event = convertMessage(msg);
        TransactionHandler handler = TransactionHandlerCache.get(event);
        TransactionStatus status = handler.execute(event);
        return convertStatus(status);
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Event event = convertMessage(msg);
        TransactionHandler handler = TransactionHandlerCache.get(event);
        TransactionStatus status = handler.check(event);
        return convertStatus(status);
    }

    private static Event convertMessage(Message message) {
        return Jackson.toObject(message.getBody(), Event.class);
    }

    private static LocalTransactionState convertStatus(TransactionStatus status) {
        return switch (status) {
            case COMMIT -> LocalTransactionState.COMMIT_MESSAGE;
            case ROLLBACK -> LocalTransactionState.ROLLBACK_MESSAGE;
            default -> LocalTransactionState.UNKNOW;
        };
    }
}
