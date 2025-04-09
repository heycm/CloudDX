package com.cloudx.platform.apisign.repository;

import java.util.concurrent.TimeUnit;

/**
 * 随机字符存储层
 */
public interface NonceRepository {

    /**
     * 如果不存在则保存该随机字符
     * @param tenantId 租户ID
     * @param nonce    随机字符
     * @param timeout  存储时长，过期删除
     * @param timeUnit 存储时长单位
     * @return true-保存成功，false-保存失败，已存在该随机字符，考虑请求重放攻击
     */
    boolean saveIfAbsent(String tenantId, String nonce, long timeout, TimeUnit timeUnit);
}
