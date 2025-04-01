package com.cloudx.common.tools;

import java.util.UUID;

/**
 * UUID
 * @author hey
 * @version 1.0
 * @since 2025/2/20 13:46
 */
public class UUIDUtil {

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

}
