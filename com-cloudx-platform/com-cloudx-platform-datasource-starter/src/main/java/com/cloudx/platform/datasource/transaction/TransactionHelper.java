package com.cloudx.platform.datasource.transaction;

import com.cloudx.common.entity.error.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 编程式事务工具
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 21:20
 */
@Slf4j
public class TransactionHelper {

    private static volatile PlatformTransactionManager platformTransactionManager;

    private TransactionHelper() {}

    public static void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        if (TransactionHelper.platformTransactionManager == null) {
            TransactionHelper.platformTransactionManager = platformTransactionManager;
        }
    }

    /**
     * 注册事务钩子
     * @param hooks 钩子函数
     */
    public static void hooks(TransactionSynchronization... hooks) {
        // 注册事务钩子当前执行线程必须处于活动事务上下文中
        if (hooks != null && hooks.length > 0 && TransactionSynchronizationManager.isSynchronizationActive()) {
            for (TransactionSynchronization hook : hooks) {
                TransactionSynchronizationManager.registerSynchronization(hook);
            }
        }
    }

    /**
     * 事务执行器
     * @param function    业务单元
     * @param propagation 事务传播行为
     * @param hooks       事务钩子
     */
    public static void transactingThrows(TransactionFunction function, int propagation, TransactionSynchronization... hooks) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(propagation);
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        try {
            function.apply();
            hooks(hooks);
            platformTransactionManager.commit(status);
            if (log.isDebugEnabled()) {
                log.debug("TransactionHelper transaction commit.");
            }
        } catch (ServiceException e) {
            platformTransactionManager.rollback(status);
            throw e;
        } catch (Exception e) {
            log.error("TransactionHelper transaction rollback for error: {}", e.getMessage(), e);
            platformTransactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 事务执行器
     * @param function    业务单元
     * @param propagation 事务传播行为
     * @param hooks       事务钩子
     * @return true-事务提交 false-事务回滚
     */
    public static boolean transacting(TransactionFunction function, int propagation, TransactionSynchronization... hooks) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(propagation);
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        try {
            function.apply();
            hooks(hooks);
            platformTransactionManager.commit(status);
            if (log.isDebugEnabled()) {
                log.debug("TransactionHelper transaction commit.");
            }
            return true;
        } catch (ServiceException e) {
            platformTransactionManager.rollback(status);
        } catch (Exception e) {
            log.error("TransactionHelper transaction rollback for error: {}", e.getMessage(), e);
            platformTransactionManager.rollback(status);
        }
        return false;
    }
}
