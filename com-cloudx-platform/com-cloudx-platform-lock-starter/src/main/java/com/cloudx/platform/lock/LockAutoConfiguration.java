package com.cloudx.platform.lock;

import com.cloudx.platform.lock.aspect.LockAspect;
import com.cloudx.platform.lock.client.LockClient;
import com.cloudx.platform.lock.client.LockClientImpl;
import com.cloudx.platform.lock.client.LockX;
import com.cloudx.platform.lock.util.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 21:31
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
@Slf4j
public class LockAutoConfiguration {

    private final RedisProperties redisProperties;

    public LockAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
        log.info("platform component [Lock] starter ready...");
    }

    /**
     * 单点模式
     */
    @Bean("redissonClient")
    @ConditionalOnProperty(value = "spring.redis.host")
    public RedissonClient standaloneRedissonClient() {
        return RedissonUtil.createStandalone(redisProperties);
    }

    /**
     * 哨兵模式
     */
    @Bean("redissonClient")
    @ConditionalOnProperty(value = "spring.redis.sentinel.nodes")
    public RedissonClient sentinelRedissonClient() {
        return RedissonUtil.createSentinel(redisProperties);
    }

    /**
     * 分片集群
     */
    @Bean("redissonClient")
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes")
    public RedissonClient clusterRedissonClient() {
        return RedissonUtil.createCluster(redisProperties);
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public LockClient lockClient(@Qualifier("redissonClient") RedissonClient redissonClient) {
        LockClientImpl lockClient = new LockClientImpl(redissonClient);
        LockX.initLockClient(lockClient);
        return lockClient;
    }

    @Bean
    @ConditionalOnBean(LockClient.class)
    public LockAspect distributedLockAspect(LockClient redisLock) {
        return new LockAspect(redisLock);
    }
}
