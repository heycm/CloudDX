package com.cloudx.platform.domain.model;

/**
 * 聚合根标识，是对一组存在引用关系对象的封装，作用是屏蔽内部复杂的关联关系，仅对外暴露统一的接口 <br/>
 * 聚合外部可以通过根对象来操作聚合，聚合内部通过执行一系列逻辑来同步各个对象的状态一致 <br/>
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 20:12
 */
public interface AggregateRoot<T extends Identifier> extends Entity<T> {

}
