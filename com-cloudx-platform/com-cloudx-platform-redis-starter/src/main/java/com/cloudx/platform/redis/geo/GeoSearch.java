package com.cloudx.platform.redis.geo;

import lombok.Data;
import org.springframework.data.geo.Distance;

import java.io.Serial;
import java.io.Serializable;

/**
 * 地理位置搜索参数
 */
@Data
public class GeoSearch implements Serializable {

    @Serial
    private static final long serialVersionUID = -5133280299994464390L;

    /**
     * 键名
     */
    private String key;

    /**
     * 搜索类型
     */
    private Form form = Form.LONLAT;

    /**
     * 中心点
     */
    private RedisGeo center;

    /**
     * 半径（距离、单位）
     */
    private Distance radius;

    /**
     * 搜索结果数量
     */
    private Long limit;

    /**
     * 按距离排序
     */
    private Sort sort = Sort.ASC;

    public enum Form {

        // 按坐标
        LONLAT,

        // 按成员
        MEMBER
    }

    public enum Sort {

        // 由近到远
        ASC,

        // 由远到近
        DESC
    }
}
