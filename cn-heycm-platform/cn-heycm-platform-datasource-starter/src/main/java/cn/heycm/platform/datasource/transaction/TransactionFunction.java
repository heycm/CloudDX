package cn.heycm.platform.datasource.transaction;

/**
 * 事务业务单元
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 21:26
 */
@FunctionalInterface
public interface TransactionFunction {

    void apply();

}
