package cn.heycm.common.tools;

import java.util.Random;

/**
 * 数字工具类
 * @author heycm
 * @version 1.0
 * @since 2025/1/7 21:32
 */
public class NumericUtil {

    private static final long MAX_VALUE_FOR_9_DIGITS = 1000000000L;
    private static final Random RANDOM = new Random();

    /**
     * 生成一个n位数的验证码
     * @param n 验证码的位数
     * @return 验证码字符串
     */
    public static String generateNumericCode(int n) {
        if (n <= 0) {
            return "";
        }

        // For a single digit, simply return a random number from 0 to 9.
        if (n == 1) {
            return String.valueOf(RANDOM.nextInt(10));
        }

        // Calculate the minimum and maximum value for an n-digit number.
        long min = (long) Math.pow(10, n - 1);
        long max = n < 10 ? (long) Math.pow(10, n) - 1 : MAX_VALUE_FOR_9_DIGITS;
        // Generate a random number within the range of [min, max].
        long code = min + (Math.abs(RANDOM.nextLong()) % (max - min + 1));
        return Long.toString(code);
    }

}
