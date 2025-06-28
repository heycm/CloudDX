package com.cloudx.common.entity.domain;

/**
 * 聚合根存储层接口
 * @author heycm
 * @version 1.0
 * @since 2025/6/28 19:42
 */
public interface Repository<T extends AggregateRoot<ID>, ID extends Identifier> {

    void save(T aggregateRoot);

    T find(ID id);
}
