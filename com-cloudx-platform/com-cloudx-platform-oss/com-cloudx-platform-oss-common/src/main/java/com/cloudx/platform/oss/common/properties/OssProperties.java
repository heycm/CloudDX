package com.cloudx.platform.oss.common.properties;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * OSS节点配置
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:13
 */
@Data
public class OssProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 7560204603320449322L;

    /*
     * OSS 端点
     */
    private String endpoint;

    /*
     * 存储桶区域
     */
    private String region;

    /*
     * 存储桶名称
     */
    private String bucketName;

    /*
     * 访问密钥
     */
    private String accessKey;

    /*
     * 访问密钥
     */
    private String secretKey;

    /*
     * 路径风格
     * true: http://bucketName.endpoint/objectName  阿里云需要这种格式 virtual-hosted-style
     * false: http://endpoint/bucketName/objectName
     */
    private boolean pathStyleAccess = true;

    /*
     * 协议：http/https
     */
    private String protocol = "https";

    /*
     * 建立连接超时时间
     */
    private int connectionTimeout = 3000;

    /*
     * Socket层传输数据超时时间
     */
    private int socketTimeout = 10000;

    /*
     * 允许打开的最大HTTP连接数
     */
    private int maxConnections = 20;

    /*
     * 请求失败后最大的重试次数
     */
    private int maxErrorRetry = 3;

}
