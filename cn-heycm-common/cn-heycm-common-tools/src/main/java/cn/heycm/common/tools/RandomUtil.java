package cn.heycm.common.tools;

import java.security.SecureRandom;

/**
 * 随机数工具
 * @author heycm
 * @version 1.0
 * @since 2025/2/21 21:13
 */
public class RandomUtil {

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";

    /**
     * 生成随机字符串
     * @param length 字符串长度
     * @return
     */
    public static String generate(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            key.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return key.toString();
    }
}
