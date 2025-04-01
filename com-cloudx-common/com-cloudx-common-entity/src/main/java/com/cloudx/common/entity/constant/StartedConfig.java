package com.cloudx.common.entity.constant;

/**
 * 启动配置项，必须完成此系列配置，否则可能导致启动失败或运行异常
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 20:17
 */
public interface StartedConfig {

    // 启动项目时 -Dcipher.aes.key=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 指定 AES 加密密钥，否则使用默认密钥，可能会导致加解密失败
    String AES_KEY = "cipher.aes.key";

    // 启动项目时 -Dcipher.aes.iv=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 指定 AES 加密向量，否则使用默认向量，可能会导致加解密失败
    String AES_IV = "cipher.aes.iv";

}
