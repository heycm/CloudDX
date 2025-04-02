package com.cloudx.platform.redis.geo;

import lombok.Data;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.io.Serial;
import java.io.Serializable;

/**
 * GEO 数据模型
 */
@Data
public class RedisGeo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3094005633367460822L;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 成员
     */
    private Object member;

    /**
     * 距离
     */
    private Distance distance;

    public Point getPoint() {
        return new Point(longitude, latitude);
    }
}
