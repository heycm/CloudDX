package com.cloudx.common.entity.error;

import java.util.function.Supplier;

/**
 * 断言工具
 * @author heycm
 * @version 1.0
 * @since 2025-3-22 20:27
 */
public class Assert {

    private Assert() {
    }

    public static void isTrue(boolean expression, Supplier<ServiceException> supplier) {
        if (!expression) {
            throw supplier.get();
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ServiceException(message);
        }
    }

    public static void isTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new ServiceException(errorCode);
        }
    }

    public static void notNull(Object object, String message) {
        Assert.isTrue(object != null, message);
    }

    public static void notNull(Object object, ErrorCode errorCode) {
        Assert.isTrue(object != null, errorCode);
    }

    public static void notBlank(String str, String message) {
        Assert.isTrue(str != null && !str.isBlank(), message);
    }

    public static void notBlank(String str, ErrorCode errorCode) {
        Assert.isTrue(str != null && !str.isBlank(), errorCode);
    }

    public static void isNull(Object object, String message) {
        Assert.isTrue(object == null, message);
    }

    public static void isNull(Object object, ErrorCode errorCode) {
        Assert.isTrue(object == null, errorCode);
    }

    public static void isBlank(String str, String message) {
        Assert.isTrue(str == null || str.isBlank(), message);
    }

    public static void isBlank(String str, ErrorCode errorCode) {
        Assert.isTrue(str == null || str.isBlank(), errorCode);
    }

}
