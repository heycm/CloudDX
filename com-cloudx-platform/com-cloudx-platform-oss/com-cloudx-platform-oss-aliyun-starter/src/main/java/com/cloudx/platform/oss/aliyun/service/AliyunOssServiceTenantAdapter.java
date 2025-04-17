package com.cloudx.platform.oss.aliyun.service;

import com.cloudx.common.entity.tenant.TenantContextHolder;
import com.cloudx.platform.oss.common.entity.AccessControlList;
import com.cloudx.platform.oss.common.entity.Bucket;
import com.cloudx.platform.oss.common.entity.ObjectMetadata;
import com.cloudx.platform.oss.common.entity.OssObject;
import com.cloudx.platform.oss.common.factory.OssServiceFactory;
import com.cloudx.platform.oss.common.properties.OssProperties;
import com.cloudx.platform.oss.common.properties.TenantOssProperties;
import com.cloudx.platform.oss.common.service.OssService;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多租户适配
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 19:17
 */
public class AliyunOssServiceTenantAdapter implements OssService {

    private final Map<String, OssService> services = new ConcurrentHashMap<>();
    private final TenantOssProperties tenantOssProperties;
    private final OssServiceFactory ossServiceFactory;

    public AliyunOssServiceTenantAdapter(TenantOssProperties tenantOssProperties, OssServiceFactory ossServiceFactory) {
        this.tenantOssProperties = tenantOssProperties;
        this.ossServiceFactory = ossServiceFactory;
    }

    private OssService getTenantOssService() {
        String tenantId = TenantContextHolder.getTenantId();
        return services.computeIfAbsent(tenantId, (key) -> {
            OssProperties properties = tenantOssProperties.getTenant().get(key);
            return ossServiceFactory.create(properties);
        });
    }

    @Override
    public boolean isBucketExist() {
        return this.getTenantOssService().isBucketExist();
    }

    @Override
    public boolean createBucket() {
        return this.getTenantOssService().createBucket();
    }

    @Override
    public boolean deleteBucket() {
        return this.getTenantOssService().deleteBucket();
    }

    @Override
    public Bucket getBucket() {
        return this.getTenantOssService().getBucket();
    }

    @Override
    public List<Bucket> listBuckets() {
        return this.getTenantOssService().listBuckets();
    }

    @Override
    public boolean setBucketAcl(AccessControlList acl) {
        return this.getTenantOssService().setBucketAcl(acl);
    }

    @Override
    public AccessControlList getBucketAcl() {
        return this.getTenantOssService().getBucketAcl();
    }

    @Override
    public boolean setObjectAcl(String objectName, AccessControlList.ACL acl) {
        return this.getTenantOssService().setObjectAcl(objectName, acl);
    }

    @Override
    public AccessControlList setObjectAcl(String objectName) {
        return this.getTenantOssService().setObjectAcl(objectName);
    }

    @Override
    public boolean setBucketPolicy(String policyJson) {
        return this.getTenantOssService().setBucketPolicy(policyJson);
    }

    @Override
    public String getBucketPolicy() {
        return this.getTenantOssService().getBucketPolicy();
    }

    @Override
    public boolean putObject(String objectName, File file) {
        return this.getTenantOssService().putObject(objectName, file);
    }

    @Override
    public boolean putObject(String objectName, File file, ObjectMetadata metadata) {
        return this.getTenantOssService().putObject(objectName, file, metadata);
    }

    @Override
    public boolean putObject(String objectName, File file, AccessControlList.ACL acl) {
        return this.getTenantOssService().putObject(objectName, file, acl);
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream) {
        return this.getTenantOssService().putObject(objectName, inputStream);
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream, ObjectMetadata metadata) {
        return this.getTenantOssService().putObject(objectName, inputStream, metadata);
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream, AccessControlList.ACL acl) {
        return this.getTenantOssService().putObject(objectName, inputStream, acl);
    }

    @Override
    public OssObject getObject(String objectName) {
        return this.getTenantOssService().getObject(objectName);
    }

    @Override
    public boolean deleteObject(String objectName) {
        return this.getTenantOssService().deleteObject(objectName);
    }

    @Override
    public boolean isObjectExist(String objectName) {
        return this.getTenantOssService().isObjectExist(objectName);
    }

    @Override
    public ObjectMetadata getObjectMetadata(String objectName) {
        return this.getTenantOssService().getObjectMetadata(objectName);
    }

    @Override
    public boolean copyObject(String sourceObjectName, String targetObjectName) {
        return this.getTenantOssService().copyObject(sourceObjectName, targetObjectName);
    }

    @Override
    public List<String> listObject() {
        return this.getTenantOssService().listObject();
    }

    @Override
    public List<String> listObject(int maximum) {
        return this.getTenantOssService().listObject(maximum);
    }

    @Override
    public List<String> listObject(String prefix) {
        return this.getTenantOssService().listObject(prefix);
    }

    @Override
    public List<String> listObject(String prefix, int maximum) {
        return this.getTenantOssService().listObject(prefix, maximum);
    }

    @Override
    public String getPresignedUrl(String objectName) {
        return this.getTenantOssService().getPresignedUrl(objectName);
    }

    @Override
    public String getPresignedUrl(String objectName, int seconds) {
        return this.getTenantOssService().getPresignedUrl(objectName, seconds);
    }

    @Override
    public String getPresignedUrl(String objectName, int seconds, String method) {
        return this.getTenantOssService().getPresignedUrl(objectName, seconds, method);
    }

    @Override
    public String getUrl(String objectName) {
        return this.getTenantOssService().getUrl(objectName);
    }

    @Override
    public String getObjectName(String url) {
        return this.getTenantOssService().getObjectName(url);
    }

    @Override
    public String getScaleImg(String url, int percentage) {
        return this.getTenantOssService().getScaleImg(url, percentage);
    }

    @Override
    public String getUploadUrl(String objectName) {
        return this.getTenantOssService().getUploadUrl(objectName);
    }

    @Override
    public String getUploadUrl(String objectName, int seconds) {
        return this.getTenantOssService().getUploadUrl(objectName, seconds);
    }

    @Override
    public boolean uploadDirectory(String objectNamePrefix, File file) {
        return this.getTenantOssService().uploadDirectory(objectNamePrefix, file);
    }

    @Override
    public boolean uploadDeepDirectory(String objectNamePrefix, File file) {
        return this.getTenantOssService().uploadDeepDirectory(objectNamePrefix, file);
    }

    @Override
    public boolean download(String objectName, File file) {
        return this.getTenantOssService().download(objectName, file);
    }
}
