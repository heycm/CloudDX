package cn.heycm.common.tools.cipher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 * @author heycm
 * @version 1.0
 * @since 2024/12/13 21:25
 */
public class MD5Util {

    private static final String ALGORITHM = "MD5";

    /**
     * MD5加密
     * @param content 内容
     * @return 密文
     */
    public static String encode(String content) {
        return MD5Util.encode(content, null);
    }

    /**
     * MD5加密
     * @param content 内容
     * @param salt    盐
     * @return 密文
     */
    public static String encode(String content, String salt) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            if (salt != null && !salt.isBlank()) {
                md.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] digest = md.digest(content.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
