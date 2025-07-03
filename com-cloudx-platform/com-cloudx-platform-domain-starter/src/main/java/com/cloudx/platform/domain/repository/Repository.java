package com.cloudx.platform.domain.repository;


import com.cloudx.platform.domain.model.AggregateRoot;
import com.cloudx.platform.domain.model.Identifier;

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
