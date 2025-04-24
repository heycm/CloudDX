package com.cloudx.common.tools.cipher;

import com.cloudx.common.entity.constant.StartedConfig;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
     * AES 加密
     * @param data      原数据
     * @param secretKey 加密密钥
     * @param iv        加密向量
     * @return 加密数据
     */
    public static String encrypt(String data, String secretKey, String iv) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), ALGORITHM), new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted); // 返回Base64编码的密文
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * AES 解密
     * @param encryptedData 加密数据
     * @param secretKey     加密密钥
     * @param iv            加密向量
     * @return 原数据
     */
    public static String decrypt(String encryptedData, String secretKey, String iv) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), ALGORITHM), new IvParameterSpec(iv.getBytes()));
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData.getBytes(StandardCharsets.UTF_8));
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 加密
     * @param content 明文
     * @return 密文
     */
    public static String encrypt(String content) {
        return AESUtil.encrypt(content, SECRET_KEY, IV);
    }

    /**
     * 解密
     * @param encryptedData 密文
     * @return 明文
     */
    public static String decrypt(String encryptedData) {
        return AESUtil.decrypt(encryptedData, SECRET_KEY, IV);
    }
}
