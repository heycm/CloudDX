package cn.heycm.platform.mybatis.plugins.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SQL染色插件上下文：用于业务拓展染色信息
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 22:59
 */
public class SQLMarkingContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public static void put(String key, Object value) {
        Map<String, Object> ctx = CONTEXT.get();
        if (ctx == null) {
            ctx = new LinkedHashMap<>();
        }
        ctx.put(key, value);
    }

    public static Map<String, Object> get() {
        Map<String, Object> data = CONTEXT.get();
        return data == null ? Collections.emptyMap() : data;
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
