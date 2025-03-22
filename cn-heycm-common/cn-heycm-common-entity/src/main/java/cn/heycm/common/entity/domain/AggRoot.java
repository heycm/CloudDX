package cn.heycm.common.entity.domain;

/**
 * 聚合根标识
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 20:12
 */
public interface AggRoot<T> {

    /**
     * 获取聚合根ID
     * @return ID
     */
    T getIdentifier();

}
