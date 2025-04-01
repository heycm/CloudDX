package com.cloudx.common.web.call;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用参数，为了方便组织多个零散的调用参数而设计，仅用于日志输出，不得作为真正请求参数，建议将远程/调用多参数整合为一个对象
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 22:40
 */
public class CallArgument {

    private final Map<String, Object> data = new HashMap<>();

    private final String name;

    public CallArgument(String name) {
        this.name = name;
    }

    public static CallArgument create(String name) {
        return new CallArgument(name);
    }

    public CallArgument put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public String name() {
        return name;
    }

    public Map<String, Object> data() {
        return data;
    }
}
