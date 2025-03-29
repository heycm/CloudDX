package cn.heycm.platform.mq.common.constant;

import lombok.Getter;

/**
 * MQ事务消息：本地事务执行状态
 * @author heycm
 * @version 1.0
 * @since 2025/3/29 23:02
 */
@Getter
public enum TransactionStatus {

    /**
     * 未知
     */
    UNKNOWN(0),

    /**
     * 已提交
     */
    COMMIT(1),

    /**
     * 已回滚
     */
    ROLLBACK(2);

    /**
     * 状态
     */
    private final int status;

    TransactionStatus(int status) {
        this.status = status;
    }
}
