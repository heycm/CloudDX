package com.cloudx.platform.redis.util;

import com.cloudx.common.tools.Jackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisUtil {

    /**
     * Jedis连接池
     */
    public static JedisClientConfiguration createJedisConf(RedisProperties properties) {
        RedisProperties.Pool pool = properties.getJedis().getPool();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMaxWait(pool.getMaxWait());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        return JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig).and().readTimeout(properties.getTimeout()).build();
    }

    /**
     * 单点Redis
     */
    public static RedisStandaloneConfiguration createStandaloneConf(RedisProperties properties) {
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setHostName(properties.getHost());
        conf.setPort(properties.getPort());
        conf.setDatabase(properties.getDatabase());
        if (StringUtils.hasText(properties.getUsername())) {
            conf.setUsername(properties.getUsername());
        }
        if (StringUtils.hasText(properties.getPassword())) {
            conf.setPassword(properties.getPassword());
        }
        return conf;
    }

    /**
     * 哨兵集群
     */
    public static RedisSentinelConfiguration createSentinelConf(RedisProperties properties) {
        RedisProperties.Sentinel sentinel = properties.getSentinel();
        Set<RedisNode> nodes = RedisUtil.getRedisNodes(sentinel.getNodes());
        RedisSentinelConfiguration conf = new RedisSentinelConfiguration();
        conf.setSentinels(nodes);
        conf.setMaster(sentinel.getMaster());
        conf.setDatabase(properties.getDatabase());
        if (StringUtils.hasText(properties.getUsername())) {
            conf.setUsername(properties.getUsername());
        }
        if (StringUtils.hasText(properties.getPassword())) {
            conf.setPassword(properties.getPassword());
        }
        if (StringUtils.hasText(sentinel.getUsername())) {
            conf.setSentinelUsername(sentinel.getUsername());
        }
        if (StringUtils.hasText(sentinel.getPassword())) {
            conf.setSentinelPassword(sentinel.getPassword());
        }
        return conf;
    }

    /**
     * 分片集群
     */
    public static RedisClusterConfiguration createClusterConf(RedisProperties properties) {
        RedisProperties.Cluster cluster = properties.getCluster();
        Set<RedisNode> nodes = RedisUtil.getRedisNodes(cluster.getNodes());
        RedisClusterConfiguration conf = new RedisClusterConfiguration();
        conf.setClusterNodes(nodes);
        conf.setMaxRedirects(cluster.getMaxRedirects());
        if (StringUtils.hasText(properties.getUsername())) {
            conf.setUsername(properties.getUsername());
        }
        if (StringUtils.hasText(properties.getPassword())) {
            conf.setPassword(properties.getPassword());
        }
        return conf;
    }

    /**
     * RedisTemplate
     */
    public static RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 键序列化策略
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        // 值序列化策略
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setDefaultSerializer(valueSerializer);
        return redisTemplate;
    }

    private static Set<RedisNode> getRedisNodes(List<String> nodes) {
        return nodes.stream().map(node -> {
            String[] split = node.split(":");
            return new RedisNode(split[0], Integer.parseInt(split[1]));
        }).collect(Collectors.toSet());
    }
}
