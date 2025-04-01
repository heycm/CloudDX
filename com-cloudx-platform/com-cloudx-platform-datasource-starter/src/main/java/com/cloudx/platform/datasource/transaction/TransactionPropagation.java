package com.cloudx.platform.datasource.transaction;

import com.cloudx.platform.datasource.transaction.strategy.PropagationStrategy;
import com.cloudx.platform.datasource.transaction.strategy.PropagationStrategyImpl;
import org.springframework.transaction.TransactionDefinition;

/**
 * 事务传播行为枚举类
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 22:18
 */
public enum TransactionPropagation {

    /**
     * 默认事务传递：加入当前事务或新建事务
     */
    REQUIRES(new PropagationStrategyImpl(TransactionDefinition.PROPAGATION_REQUIRED)),

    /**
     * 新事务创建：创建新事务，如果当前存在事务，则挂起当前事务
     */
    REQUIRES_NEW(new PropagationStrategyImpl(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

    private final PropagationStrategy propagationStrategy;

    TransactionPropagation(PropagationStrategy propagationStrategy) {
        this.propagationStrategy = propagationStrategy;
    }
}
