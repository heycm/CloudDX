package cn.heycm.platform.mq.common.handler;

import cn.heycm.platform.mq.common.constant.TransactionStatus;
import cn.heycm.platform.mq.common.event.Event;

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
