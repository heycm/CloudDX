package cn.heycm.platform.datasource.transaction.strategy;

import cn.heycm.platform.datasource.transaction.TransactionFunction;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * 事务传播策略接口
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 22:05
 */
public interface PropagationStrategy {

    /**
     * 执行事务
     * @param function 业务单元
     * @param hooks    事务钩子
     * @return true-事务提交 false-事务回滚
     */
    boolean execute(TransactionFunction function, TransactionSynchronization... hooks);

    /**
     * 执行事务
     * @param function 业务单元
     * @param hooks    事务钩子
     * @throws Exception 业务单元执行异常
     */
    void executeThrows(TransactionFunction function, TransactionSynchronization... hooks);

}
