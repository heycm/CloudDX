package com.cloudx.platform.redis.client;

import com.cloudx.platform.redis.geo.GeoSearch;
import com.cloudx.platform.redis.geo.RedisGeo;
import org.springframework.data.geo.Distance;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 客户端
 */
public interface RedisClient {

    /**
     * 查询指定键是否存在
     * @param key 键
     * @return true 存在 false 不存在
     */
    boolean hasKey(String key);

    /**
     * 删除指定键
     * @param keys 键
     * @return 删除数量
     */
    long del(String... keys);

    /**
     * 设置指定键过期时间
     * @param key      键
     * @param time     时长
     * @param timeUnit 时间单位
     * @return true 成功 false 失败
     */
    boolean expire(String key, long time, TimeUnit timeUnit);

    /**
     * 设置指定键过期时间，单位秒
     * @param key     键
     * @param seconds 秒数
     * @return true 成功 false 失败
     */
    boolean expire(String key, long seconds);

    /**
     * 返回指定键过期时间
     * @param key 键
     * @return 毫秒数
     */
    long getExpire(String key);

    /**
     * 执行指定脚本
     * @param script 脚本
     * @param keys   KEYS[123...]
     * @param args   ARGV[123...]
     * @return T
     */
    <T> T execute(RedisScript<T> script, List<String> keys, Object... args);

    /**
     * [String] 设置指定键值
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    boolean set(String key, String value);

    /**
     * [String] 设置指定键值，并设置过期时间
     * @param key      键
     * @param value    值
     * @param time     时长
     * @param timeUnit 时间单位
     * @return true 成功 false 失败
     */
    boolean set(String key, String value, long time, TimeUnit timeUnit);

    /**
     * [String] 如果键不存在，则设置键的值，如果键已经存在，则不进行任何操作
     * @param key     键
     * @param value   值
     * @param seconds 秒数
     * @return true 成功 false 失败
     */
    boolean setNx(String key, String value, long seconds);

    /**
     * [String] 如果键不存在，则设置键的值，如果键已经存在，则不进行任何操作
     * @param key      键
     * @param value    值
     * @param time     时长
     * @param timeUnit 时间单位
     * @return true 成功 false 失败
     */
    boolean setNx(String key, String value, long time, TimeUnit timeUnit);

    /**
     * [String] 返回指定键值
     * @param key 键
     * @return 值
     */
    String get(String key);

    /**
     * [String] 返回指定键值
     * @param key 键
     * @return 值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * [String] 批量获取指定键值，按原始顺序返回，不存在的键值返回 null
     * @param keys 键集合
     * @return 值集合
     */
    <T> List<T> multiGet(List<String> keys, Class<T> clazz);

    /**
     * [String] 顺序递增，步长为 1
     * @param key 键
     * @return 递增后的值
     */
    long incr(String key);

    /**
     * [String] 指定步长顺序递增
     * @param key   键
     * @param delta 步长
     * @return 递增后的值
     */
    long incr(String key, long delta);

    /**
     * [String] 顺序递减，步长为 1
     * @param key 键
     * @return 递减后的值
     */
    long decr(String key);

    /**
     * [String] 指定步长顺序递减
     * @param key   键
     * @param delta 步长
     * @return 递减后的值
     */
    long decr(String key, long delta);

    /**
     * [Hash] 判断Hash表是否存在元素
     * @param key  键
     * @param item 元素
     * @return true 存在 false 不存在
     */
    boolean hHasItem(String key, String item);

    /**
     * [Hash] 创建Hash表
     * @param key   键
     * @param value Hash表
     * @return true 成功 false 失败
     */
    <T> boolean hSet(String key, T value);

    /**
     * [Hash] 创建Hash表，并设置过期时间
     * @param key      键
     * @param value    Hash表
     * @param time     时长
     * @param timeUnit 时间单位
     * @return true 成功 false 失败
     */
    <T> boolean hSet(String key, T value, long time, TimeUnit timeUnit);

    /**
     * [Hash] 设置Hash表元素
     * @param key   键
     * @param item  元素
     * @param value 元素值
     * @return true 成功 false 失败
     */
    boolean hSet(String key, String item, Object value);

    /**
     * [Hash] 设置Hash表元素，如果元素不存在，则设置元素的值，如果元素已经存在，则不进行任何操作
     * @param key   键
     * @param item  元素
     * @param value 元素值
     * @return true 成功 false 失败
     */
    boolean hSetNx(String key, String item, Object value);

    /**
     * [Hash] Hash表元素值递增，步长为 1
     * @param key  键
     * @param item 元素
     * @return 递增后的值
     */
    long hIncr(String key, String item);

    /**
     * [Hash] Hash表元素值递增，指定步长
     * @param key   键
     * @param item  元素
     * @param delta 步长
     * @return 递增后的值
     */
    long hIncr(String key, String item, long delta);

    /**
     * [Hash] Hash表元素值递减，步长为 1
     * @param key  键
     * @param item 元素
     * @return 递减后的值
     */
    long hDecr(String key, String item);

    /**
     * [Hash] Hash表元素值递减，指定步长
     * @param key   键
     * @param item  元素
     * @param delta 步长
     * @return 递减后的值
     */
    long hDecr(String key, String item, long delta);

    /**
     * [Hash] 返回Hash表
     * @param key   键
     * @param clazz Hash表类型
     * @return Hash表
     */
    <T> T hGet(String key, Class<T> clazz);

    /**
     * [Hash] 返回Hash表元素
     * @param key  键
     * @param item 元素
     * @return Hash表元素
     */
    Object hGetItem(String key, String item);

    /**
     * [Hash] 返回Hash表多个元素，按顺序返回，不存在的元素返回 null
     * @param key   键
     * @param items 元素集合
     * @return 值集合
     */
    List<Object> hGetItems(String key, List<String> items);

    /**
     * [Hash] 删除Hash表多个元素
     * @param key   键
     * @param items 移除元素
     * @return 移除的元素个数
     */
    long hDelItems(String key, String... items);

    /**
     * [Set] 添加Set元素
     * @param key    键
     * @param values 元素
     * @return 添加的元素个数
     */
    long sAdd(String key, Object... values);

    /**
     * [Set] 添加Set元素
     * @param key    键
     * @param values 元素
     * @return 添加的元素个数
     */
    long sAdd(String key, Set<Object> values);

    /**
     * [Set] 删除Set元素
     * @param key    键
     * @param values 删除元素
     * @return 删除的元素个数
     */
    long sDel(String key, Object... values);

    /**
     * [Set] 获取Set元素个数
     * @param key 键
     * @return 元素个数
     */
    long sSize(String key);

    /**
     * [Set] 判断Set元素是否存在
     * @param key   键
     * @param value 元素
     * @return true 存在 false 不存在
     */
    boolean sHasValue(String key, Object value);

    /**
     * [Set] 获取Set所有元素
     * @param key 键
     * @return 元素集合
     */
    Set<Object> sGet(String key);

    /**
     * [List] 从List集合尾部添加元素
     * @param key 键
     * @return 元素集合
     */
    long lAdd(String key, Object... values);

    /**
     * [List] 从List集合尾部添加元素
     * @param key 键
     * @return 元素集合
     */
    long lAdd(String key, List<Object> values);

    /**
     * [List] 从List集合头部添加元素
     * @param key 键
     * @return 元素集合
     */
    long lLeftAdd(String key, Object... values);

    /**
     * [List] 从List集合头部添加元素
     * @param key 键
     * @return 元素集合
     */
    long lLeftAdd(String key, List<Object> values);

    /**
     * [List] 获取List集合
     * @param key 键
     * @return 元素集合
     */
    List<Object> lGet(String key);

    /**
     * [List] 获取List集合
     * @param key        键
     * @param startIndex 开始索引，0 表示第一个元素
     * @param endIndex   结束索引，-1 表示最后一个元素
     * @return 元素集合
     */
    List<Object> lGet(String key, long startIndex, long endIndex);

    /**
     * [List] 获取List集合大小
     * @param key 键
     * @return 集合长度
     */
    long lSize(String key);

    /**
     * [List] 获取List集合指定元素索引
     * @param key   键
     * @param value 元素值
     * @return 元素索引，-1 表示不存在
     */
    long lIndexOf(String key, Object value);

    /**
     * [List] 获取List集合指定元素
     * @param key   键
     * @param index 元素索引
     * @return 元素值
     */
    Object lGetIndex(String key, long index);

    /**
     * [List] 更新List集合指定索引的元素
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true 成功 false 失败
     */
    boolean lSetIndex(String key, long index, Object value);

    /**
     * [List] 删除List集合指定元素
     * @param key   键
     * @param value 元素值
     * @return 删除的元素个数
     */
    long lDel(String key, Object value);

    /**
     * [List] 从List集合头部弹出一个元素
     * @param key 键
     * @return 元素值
     */
    Object lPop(String key);

    /**
     * [List] 从List集合尾部弹出一个元素
     * @param key 键
     * @return 元素值
     */
    Object lRightPop(String key);

    /**
     * [ZSet] 添加ZSet元素
     * @param key   键
     * @param value 元素
     * @param score 分数
     * @return true 成功 false 失败
     */
    boolean zAdd(String key, Object value, double score);

    /**
     * [ZSet] 获取ZSet元素
     * @param key 键
     * @return 元素集合
     */
    Set<Object> zGet(String key);

    /**
     * [ZSet] 删除ZSet元素
     * @param key    键
     * @param values 值
     * @return 删除的元素个数
     */
    long zDel(String key, Object... values);

    /**
     * [ZSet] 获取ZSet元素个数
     * @param key 键
     * @return 集合长度
     */
    long zSize(String key);

    /**
     * [ZSet] 获取ZSet元素分数
     * @param key   键
     * @param value 元素
     * @return 分数
     */
    Double zScore(String key, Object value);

    /**
     * [ZSet] 通过分数获取ZSet元素
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    Set<Object> zGetByScore(String key, double min, double max);

    /**
     * [ZSet] 弹出1个最大分数元素
     * @param key 键
     * @return 弹出元素
     */
    Object zPopMax(String key);

    /**
     * [ZSet] 弹出多个最大分数元素
     * @param key   键
     * @param count 弹出个数
     * @return 弹出元素集合
     */
    Set<Object> zPopMax(String key, int count);

    /**
     * [ZSet] 弹出1个最小分数元素
     * @param key 键
     * @return 弹出元素
     */
    Object zPopMin(String key);

    /**
     * [ZSet] 弹出多个最小分数元素
     * @param key   键
     * @param count 弹出个数
     * @return 弹出元素集合
     */
    Set<Object> zPopMin(String key, int count);

    /**
     * [Geo] 添加Geo成员
     * @param key       键
     * @param longitude 经度
     * @param latitude  纬度
     * @param member    成员
     * @return true 成功 false 失败
     */
    long gAdd(String key, double longitude, double latitude, Object member);

    /**
     * [Geo] 添加Geo成员
     * @param key       键
     * @param redisGeos 成员
     * @return 添加的成员个数
     */
    long gAdd(String key, RedisGeo... redisGeos);

    /**
     * [Geo] 获取两个成员的距离，单位为米
     * @param key     键
     * @param member1 成员1
     * @param member2 成员2
     * @return 距离，米
     */
    Distance gDistance(String key, Object member1, Object member2);

    /**
     * [Geo] 获取两个成员的距离，单位为指定单位
     * @param key          键
     * @param member1      成员1
     * @param member2      成员2
     * @param distanceUnit 距离单位
     * @return 距离
     */
    Distance gDistance(String key, Object member1, Object member2, RedisGeoCommands.DistanceUnit distanceUnit);

    /**
     * [Geo] 搜索范围成员
     * @param search 查询参数
     * @return 搜索结果
     */
    List<RedisGeo> search(GeoSearch search);

}
