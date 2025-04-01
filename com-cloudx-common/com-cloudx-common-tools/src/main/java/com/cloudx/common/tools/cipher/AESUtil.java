package com.cloudx.common.tools.cipher;

import com.cloudx.common.entity.constant.StartedConfig;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES
 * @author heycm
 * @version 1.0
 * @since 2024/12/8 20:48
 */
public class AESUtil {

    // 算法
    private static final String ALGORITHM = "AES";
    // 使用CBC模式和PKCS5Padding填充
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    // 固定的16字节密钥（16字符即128位）
    private static final String SECRET_KEY;
    // 固定的16字节IV（16字符即128位）
    private static final String IV;

    static {
        // 从系统属性中获取密钥和IV，如果没有设置，则使用默认值
        SECRET_KEY = System.getProperty(StartedConfig.AES_KEY, "tG2QPEw7PI_r(GQJz0X9@hD$deneX+Q*");
        IV = System.getProperty(StartedConfig.AES_IV, "8(w$^f<JCqEzalDX");
    }

    /**
     * 加密
     * @param content 明文
     * @return 密文
     */
    public static String encrypt(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), getIvParameterSpec());
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted); // 返回Base64编码的密文
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     * @param ciphertext 密文
     * @return 明文
     */
    public static String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), getIvParameterSpec());
            byte[] decodedBytes = Base64.getDecoder().decode(ciphertext.getBytes(StandardCharsets.UTF_8));
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 使用指定的密钥和IV
    private static SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }

    // 使用指定的IV
    private static IvParameterSpec getIvParameterSpec() {
        return new IvParameterSpec(IV.getBytes());
    }
}
