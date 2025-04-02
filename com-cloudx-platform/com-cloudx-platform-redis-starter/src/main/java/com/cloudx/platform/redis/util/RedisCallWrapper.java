package com.cloudx.platform.redis.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RedisCallWrapper {

    public static <T> T call(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("Redis call error: {}", e.getMessage(), e);
        }
        return null;
    }

    public static boolean boolCall(Supplier<Boolean> supplier) {
        Boolean call = RedisCallWrapper.call(supplier);
        return Boolean.TRUE.equals(call);
    }

    public static long longCall(Supplier<Long> supplier) {
        Long call = RedisCallWrapper.call(supplier);
        return call == null ? 0 : call;
    }

}
