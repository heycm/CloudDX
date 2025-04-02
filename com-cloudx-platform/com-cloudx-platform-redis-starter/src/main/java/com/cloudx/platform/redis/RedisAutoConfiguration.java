package com.cloudx.platform.redis;

import com.cloudx.platform.redis.aspect.LimiterAspect;
import com.cloudx.platform.redis.client.RedisClient;
import com.cloudx.platform.redis.client.TenantRedisClientImpl;
import com.cloudx.platform.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis自动配置
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
@Slf4j
public class RedisAutoConfiguration {

    private final RedisProperties redisProperties;

    public RedisAutoConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
        log.info("platform component [Redis] starter ready...");
    }

    /**
     * 单点模式
     */
    @Bean(name = "redisConnectionFactory")
    @ConditionalOnProperty(value = "spring.data.redis.host")
    public RedisConnectionFactory standaloneRedisConnectionFactory() {
        JedisClientConfiguration jedisConf = RedisUtil.createJedisConf(redisProperties);
        RedisStandaloneConfiguration standaloneConf = RedisUtil.createStandaloneConf(redisProperties);
        return new JedisConnectionFactory(standaloneConf, jedisConf);
    }

    /**
     * 哨兵集群
     */
    @Bean(name = "redisConnectionFactory")
    @ConditionalOnProperty(value = "spring.data.redis.sentinel.nodes")
    public RedisConnectionFactory sentinelRedisConnectionFactory() {
        JedisClientConfiguration jedisConf = RedisUtil.createJedisConf(redisProperties);
        RedisSentinelConfiguration sentinelConf = RedisUtil.createSentinelConf(redisProperties);
        return new JedisConnectionFactory(sentinelConf, jedisConf);
    }

    /**
     * 分片集群
     */
    @Bean(name = "redisConnectionFactory")
    @ConditionalOnProperty(value = "spring.data.redis.cluster.nodes")
    public RedisConnectionFactory clusterRedisConnectionFactory() {
        JedisClientConfiguration jedisConf = RedisUtil.createJedisConf(redisProperties);
        RedisClusterConfiguration clusterConf = RedisUtil.createClusterConf(redisProperties);
        return new JedisConnectionFactory(clusterConf, jedisConf);
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return RedisUtil.createRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisClient redisClient(RedisTemplate<String, Object> redisTemplate) {
        return new TenantRedisClientImpl(redisTemplate);
    }

    @Bean
    @ConditionalOnBean(RedisClient.class)
    public LimiterAspect limiterAspect(RedisClient redisClient) {
        return new LimiterAspect(redisClient);
    }
}
