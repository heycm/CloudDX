package com.cloudx.platform.mq.common.handler;

import com.cloudx.platform.mq.common.constant.TransactionStatus;
import com.cloudx.platform.mq.common.event.Event;

/**
 * 默认本地事务处理器
 * @author heycm
 * @version 1.0
 * @since 2025/3/29 23:51
 */
public class DefaultTransactionHandler implements TransactionHandler {

    @Override
    public TransactionStatus execute(Event event) {
        return TransactionStatus.COMMIT;
    }

    @Override
    public TransactionStatus check(Event event) {
        return TransactionStatus.COMMIT;
    }
}
