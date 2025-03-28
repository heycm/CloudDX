package cn.heycm.platform.datasource.transaction.strategy;

import cn.heycm.platform.datasource.transaction.TransactionFunction;
import cn.heycm.platform.datasource.transaction.TransactionHelper;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * 默认传播行为实现
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 22:11
 */
public class PropagationStrategyImpl implements PropagationStrategy {

    private final int propagationBehavior;

    public PropagationStrategyImpl(int propagationBehavior) {
        this.propagationBehavior = propagationBehavior;
    }

    @Override
    public boolean execute(TransactionFunction function, TransactionSynchronization... hooks) {
        return TransactionHelper.transacting(function, propagationBehavior, hooks);
    }

    @Override
    public void executeThrows(TransactionFunction function, TransactionSynchronization... hooks) {
        TransactionHelper.transactingThrows(function, propagationBehavior, hooks);
    }
}
