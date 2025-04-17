package com.cloudx.platform.oss.aliyun.factory;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.cloudx.platform.oss.aliyun.service.AliyunOssServiceImpl;
import com.cloudx.platform.oss.common.factory.OssServiceFactory;
import com.cloudx.platform.oss.common.properties.OssProperties;
import com.cloudx.platform.oss.common.service.OssService;

/**
 * 阿里云OSS服务创建工厂
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 16:19
 */
public class AliyunOssServiceFactory implements OssServiceFactory {

    @Override
    public OssService create(OssProperties properties) {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(properties.getAccessKey(), properties.getSecretKey());
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create().endpoint(properties.getProtocol() + "://" + properties.getEndpoint())
                .credentialsProvider(credentialsProvider).clientConfiguration(clientBuilderConfiguration).region(properties.getRegion())
                .build();
        return new AliyunOssServiceImpl(ossClient, properties);
    }
}
