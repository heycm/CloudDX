package com.cloudx.platform.oss.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;

/**
 * 对象元数据
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:20
 */
@Data
public class ObjectMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 4271174013960456472L;

    /**
     * 自定义元数据
     */
    private Map<String, String> userMetadata = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Header 元数据
     */
    private Map<String, Object> rawMetadata = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public void addUserMetadata(String key, String value) {
        userMetadata.put(key, value);
    }

    public void addRowMetadata(String key, Object value) {
        rawMetadata.put(key, value);
    }

    @Override
    public String toString() {
        return "[" + "userMetadata=" + userMetadata + ", rawMetadata=" + rawMetadata + ']';
    }
}
