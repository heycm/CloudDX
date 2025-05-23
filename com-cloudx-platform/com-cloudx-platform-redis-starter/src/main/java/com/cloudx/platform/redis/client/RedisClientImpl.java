package com.cloudx.platform.redis.client;

import com.cloudx.common.entity.error.Assert;
import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.redis.geo.GeoSearch;
import com.cloudx.platform.redis.geo.RedisGeo;
import com.cloudx.platform.redis.util.RedisCallWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisClientImpl implements RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisMessageListenerContainer listenerContainer;

    public RedisClientImpl(RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer listenerContainer) {
        this.redisTemplate = redisTemplate;
        this.listenerContainer = listenerContainer;
    }

    /**
     * 包装键
     */
    protected String wrapKey(String key) {
        return key;
    }

    /**
     * 包装键
     */
    private void wrapKeys(List<String> keys) {
        ListIterator<String> iterator = keys.listIterator();
        while (iterator.hasNext()) {
            iterator.set(wrapKey(iterator.next()));
        }
    }

    @Override
    public boolean hasKey(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.hasKey(wrapKey));
    }

    @Override
    public long del(String... keys) {
        Set<String> wrapKeys = Arrays.stream(keys).map(this::wrapKey).collect(Collectors.toSet());
        return RedisCallWrapper.longCall(() -> redisTemplate.delete(wrapKeys));
    }

    @Override
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.expire(wrapKey, time, timeUnit));
    }

    @Override
    public boolean expire(String key, long seconds) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.expire(wrapKey, seconds, TimeUnit.SECONDS));
    }

    @Override
    public long getExpire(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.getExpire(wrapKey));
    }

    @Override
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        wrapKeys(keys);
        return RedisCallWrapper.call(() -> redisTemplate.execute(script, keys, args));
    }

    @Override
    public boolean set(String key, String value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> {
            redisTemplate.boundValueOps(wrapKey).set(value);
            return Boolean.TRUE;
        });
    }

    @Override
    public boolean set(String key, String value, long time, TimeUnit timeUnit) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> {
            redisTemplate.boundValueOps(wrapKey).set(value, time, timeUnit);
            return Boolean.TRUE;
        });
    }

    @Override
    public boolean setNx(String key, String value, long seconds) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.boundValueOps(wrapKey).setIfAbsent(value, seconds, TimeUnit.SECONDS));
    }

    @Override
    public boolean setNx(String key, String value, long time, TimeUnit timeUnit) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.boundValueOps(wrapKey).setIfAbsent(value, time, timeUnit));
    }

    @Override
    public String get(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            Object o = redisTemplate.boundValueOps(wrapKey).get();
            return o == null ? null : o.toString();
        });
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            Object o = redisTemplate.boundValueOps(wrapKey).get();
            return o == null ? null : (T) o;
        });
    }

    @Override
    public <T> List<T> multiGet(List<String> keys, Class<T> clazz) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        wrapKeys(keys);
        return RedisCallWrapper.call(() -> {
            List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
            if (CollectionUtils.isEmpty(objects)) {
                return Collections.emptyList();
            }
            return objects.stream().map(e -> e == null ? null : (T) e).collect(Collectors.toList());
        });
    }

    @Override
    public long incr(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundValueOps(wrapKey).increment());
    }

    @Override
    public long incr(String key, long delta) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundValueOps(wrapKey).increment(delta));
    }

    @Override
    public long decr(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundValueOps(wrapKey).decrement());
    }

    @Override
    public long decr(String key, long delta) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundValueOps(wrapKey).decrement(delta));
    }

    @Override
    public boolean hHasItem(String key, String item) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.boundHashOps(wrapKey).hasKey(item));
    }

    @Override
    public <T> boolean hSet(String key, T value) {
        String wrapKey = wrapKey(key);
        Map data;
        if (value instanceof Map) {
            data = (Map) value;
        } else {
            data = Jackson.toObject(Jackson.toJson(value), Map.class);
        }
        Assert.notNull(data, "RedisClient hSet invalid value: " + value);
        return RedisCallWrapper.boolCall(() -> {
            redisTemplate.boundHashOps(wrapKey).putAll(data);
            return Boolean.TRUE;
        });
    }

    @Override
    public <T> boolean hSet(String key, T value, long time, TimeUnit timeUnit) {
        String wrapKey = wrapKey(key);
        Map data;
        if (value instanceof Map) {
            data = (Map) value;
        } else {
            data = Jackson.toObject(Jackson.toJson(value), Map.class);
        }
        return RedisCallWrapper.boolCall(() -> {
            redisTemplate.boundHashOps(wrapKey).putAll(data);
            return redisTemplate.expire(wrapKey, time, timeUnit);
        });
    }

    @Override
    public boolean hSet(String key, String item, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> {
            redisTemplate.boundHashOps(wrapKey).put(item, value);
            return Boolean.TRUE;
        });
    }

    @Override
    public boolean hSetNx(String key, String item, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.boundHashOps(wrapKey).putIfAbsent(item, value));
    }

    @Override
    public long hIncr(String key, String item) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundHashOps(wrapKey).increment(item, 1));
    }

    @Override
    public long hIncr(String key, String item, long delta) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundHashOps(wrapKey).increment(item, delta));
    }

    @Override
    public long hDecr(String key, String item) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundHashOps(wrapKey).increment(item, -1));
    }

    @Override
    public long hDecr(String key, String item, long delta) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundHashOps(wrapKey).increment(item, -delta));
    }

    @Override
    public <T> T hGet(String key, Class<T> clazz) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            Map<Object, Object> entries = redisTemplate.boundHashOps(wrapKey).entries();
            return Jackson.toObject(Jackson.toJson(entries), clazz);
        });
    }

    @Override
    public Object hGetItem(String key, String item) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundHashOps(wrapKey).get(item));
    }

    @Override
    public List<Object> hGetItems(String key, List<String> items) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            if (CollectionUtils.isEmpty(items)) {
                return Collections.emptyList();
            }
            return redisTemplate.boundHashOps(wrapKey).multiGet(new ArrayList<>(items));
        });
    }

    @Override
    public long hDelItems(String key, String... items) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> {
            Object[] tableKeys = new String[items.length];
            System.arraycopy(items, 0, tableKeys, 0, items.length);
            return redisTemplate.boundHashOps(wrapKey).delete(tableKeys);
        });
    }

    @Override
    public long sAdd(String key, Object... values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundSetOps(wrapKey).add(values));
    }

    @Override
    public long sAdd(String key, Set<Object> values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundSetOps(wrapKey).add(values));
    }

    @Override
    public long sDel(String key, Object... values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundSetOps(wrapKey).remove(values));
    }

    @Override
    public long sSize(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundSetOps(wrapKey).size());
    }

    @Override
    public boolean sHasValue(String key, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.boundSetOps(wrapKey).isMember(value));
    }

    @Override
    public Set<Object> sGet(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundSetOps(wrapKey).members());
    }

    @Override
    public long lAdd(String key, Object... values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundListOps(wrapKey).rightPushAll(values));
    }

    @Override
    public long lAdd(String key, List<Object> values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundListOps(wrapKey).rightPushAll(values));
    }

    @Override
    public long lLeftAdd(String key, Object... values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundListOps(wrapKey).leftPushAll(values));
    }

    @Override
    public long lLeftAdd(String key, List<Object> values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundListOps(wrapKey).leftPushAll(values));
    }

    @Override
    public List<Object> lGet(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundListOps(wrapKey).range(0, -1));
    }

    @Override
    public List<Object> lGet(String key, long startIndex, long endIndex) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundListOps(wrapKey).range(startIndex, endIndex));
    }

    @Override
    public long lSize(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundListOps(wrapKey).size());
    }

    @Override
    public long lIndexOf(String key, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundListOps(wrapKey).indexOf(value));
    }

    @Override
    public Object lGetIndex(String key, long index) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundListOps(wrapKey).index(index));
    }

    @Override
    public boolean lSetIndex(String key, long index, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> {
            redisTemplate.boundListOps(wrapKey).set(index, value);
            return Boolean.TRUE;
        });
    }

    @Override
    public long lDel(String key, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> {
            BoundListOperations<String, Object> ops = redisTemplate.boundListOps(wrapKey);
            if (ops.size() == null || ops.size() <= 0) {
                return 0L;
            }
            return ops.remove(ops.size(), value);
        });
    }

    @Override
    public Object lPop(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundListOps(wrapKey).leftPop());
    }

    @Override
    public Object lRightPop(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundListOps(wrapKey).rightPop());
    }

    @Override
    public boolean zAdd(String key, Object value, double score) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.boolCall(() -> redisTemplate.boundZSetOps(wrapKey).add(value, score));
    }

    @Override
    public Set<Object> zGet(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundZSetOps(wrapKey).range(0, -1));
    }

    @Override
    public long zDel(String key, Object... values) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundZSetOps(wrapKey).remove(values));
    }

    @Override
    public long zSize(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> redisTemplate.boundZSetOps(wrapKey).size());
    }

    @Override
    public Double zScore(String key, Object value) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundZSetOps(wrapKey).score(value));
    }

    @Override
    public Set<Object> zGetByScore(String key, double min, double max) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundZSetOps(wrapKey).rangeByScore(min, max));
    }

    @Override
    public Object zPopMax(String key) {
        return RedisCallWrapper.call(() -> {
            String wrapKey = wrapKey(key);
            ZSetOperations.TypedTuple<Object> tuple = redisTemplate.boundZSetOps(wrapKey).popMax();
            return tuple == null ? null : tuple.getValue();
        });
    }

    @Override
    public Set<Object> zPopMax(String key, int count) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.boundZSetOps(wrapKey).popMax(count);
            return tuples == null ? Collections.emptySet() : tuples.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toSet());
        });
    }

    @Override
    public Object zPopMin(String key) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            ZSetOperations.TypedTuple<Object> tuple = redisTemplate.boundZSetOps(wrapKey).popMin();
            return tuple == null ? null : tuple.getValue();
        });
    }

    @Override
    public Set<Object> zPopMin(String key, int count) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> {
            Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.boundZSetOps(wrapKey).popMin(count);
            return tuples == null ? Collections.emptySet() : tuples.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toSet());
        });
    }

    @Override
    public long gAdd(String key, double longitude, double latitude, Object member) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> {
            Point point = new Point(longitude, latitude);
            return redisTemplate.boundGeoOps(wrapKey).add(point, member);
        });
    }

    @Override
    public long gAdd(String key, RedisGeo... redisGeos) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.longCall(() -> {
            Map<Object, Point> map = Arrays.stream(redisGeos).collect(Collectors.toMap(RedisGeo::getMember, RedisGeo::getPoint, (o1, o2) -> o2));
            return redisTemplate.boundGeoOps(wrapKey).add(map);
        });
    }

    @Override
    public Distance gDistance(String key, Object member1, Object member2) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundGeoOps(wrapKey).distance(member1, member2, RedisGeoCommands.DistanceUnit.METERS));
    }

    @Override
    public Distance gDistance(String key, Object member1, Object member2, RedisGeoCommands.DistanceUnit distanceUnit) {
        String wrapKey = wrapKey(key);
        return RedisCallWrapper.call(() -> redisTemplate.boundGeoOps(wrapKey).distance(member1, member2, distanceUnit));
    }

    @Override
    public List<RedisGeo> search(GeoSearch search) {
        search.setKey(wrapKey(search.getKey()));
        return RedisCallWrapper.call(() -> {
            RedisGeo center = search.getCenter();
            GeoReference<Object> reference;
            if (search.getForm() == GeoSearch.Form.LONLAT) {
                reference = GeoReference.fromCoordinate(center.getLongitude(), center.getLatitude());
            } else {
                reference = GeoReference.fromMember(center.getMember());
            }
            Distance radius = search.getRadius();
            RedisGeoCommands.GeoSearchCommandArgs args = RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeCoordinates();
            if (search.getSort() != null) {
                Sort.Direction direction = search.getSort() == GeoSearch.Sort.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
                args.sort(direction);
            }
            if (search.getLimit() != null) {
                args.limit(search.getLimit());
            }
            GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.boundGeoOps(search.getKey()).search(reference, radius, args);
            if (results == null) {
                return Collections.emptyList();
            }
            List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> list = results.getContent();
            if (CollectionUtils.isEmpty(list)) {
                return Collections.emptyList();
            }
            return list.stream().filter(Objects::nonNull).map(e -> {
                RedisGeo redisGeo = new RedisGeo();
                redisGeo.setMember(e.getContent().getName());
                redisGeo.setLongitude(e.getContent().getPoint().getX());
                redisGeo.setLatitude(e.getContent().getPoint().getY());
                redisGeo.setDistance(e.getDistance());
                return redisGeo;
            }).collect(Collectors.toList());
        });
    }

    @Override
    public void publish(String channel, Object payload) {
        redisTemplate.convertAndSend(channel, payload);
    }

    @Override
    public void subscribe(String channelPattern, MessageListener listener) {
        listenerContainer.addMessageListener(listener, new PatternTopic(channelPattern));
    }
}
