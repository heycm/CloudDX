package com.cloudx.platform.lock.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.util.StringUtils;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 23:07
 */
public class RedissonUtil {

    public static RedissonClient createStandalone(RedisProperties properties) {
        String address = "redis://" + properties.getHost() + ":" + properties.getPort();
        Config config = new Config();
        config.useSingleServer().setAddress(address)
                .setUsername(StringUtils.hasText(properties.getUsername()) ? properties.getUsername() : null)
                .setPassword(StringUtils.hasText(properties.getPassword()) ? properties.getPassword() : null)
                .setDatabase(properties.getDatabase());
        return Redisson.create(config);
    }

    public static RedissonClient createSentinel(RedisProperties properties) {
        RedisProperties.Sentinel sentinel = properties.getSentinel();
        List<String> nodes = getRedisNodes(sentinel.getNodes());
        Config config = new Config();
        config.useSentinelServers().setMasterName(sentinel.getMaster()).setReadMode(ReadMode.SLAVE)
                .setUsername(StringUtils.hasText(properties.getUsername()) ? properties.getUsername() : null)
                .setPassword(StringUtils.hasText(properties.getPassword()) ? properties.getPassword() : null).setSentinelAddresses(nodes);
        return Redisson.create(config);
    }

    public static RedissonClient createCluster(RedisProperties properties) {
        RedisProperties.Cluster cluster = properties.getCluster();
        List<String> nodes = getRedisNodes(cluster.getNodes());
        Config config = new Config();
        config.useClusterServers().setUsername(StringUtils.hasText(properties.getUsername()) ? properties.getUsername() : null)
                .setPassword(StringUtils.hasText(properties.getPassword()) ? properties.getPassword() : null).setNodeAddresses(nodes);
        return Redisson.create(config);
    }

    private static List<String> getRedisNodes(List<String> nodes) {
        return nodes.stream().map(node -> {
            if (node.startsWith("redis://")) {
                return node;
            } else {
                return "redis://" + node;
            }
        }).toList();
    }
}
