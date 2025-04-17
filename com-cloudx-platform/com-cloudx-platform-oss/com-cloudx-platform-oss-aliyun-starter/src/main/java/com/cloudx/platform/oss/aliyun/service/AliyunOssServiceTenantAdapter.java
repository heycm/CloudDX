package com.cloudx.platform.oss.aliyun.service;

import com.cloudx.platform.oss.common.entity.AccessControlList;
import com.cloudx.platform.oss.common.entity.Bucket;
import com.cloudx.platform.oss.common.entity.ObjectMetadata;
import com.cloudx.platform.oss.common.entity.OssObject;
import com.cloudx.platform.oss.common.factory.OssServiceFactory;
import com.cloudx.platform.oss.common.service.OssService;
import com.cloudx.platform.oss.common.service.OssServiceTenantAdapter;

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
public class AliyunOssServiceTenantAdapter implements OssServiceTenantAdapter {

    private final Map<String, OssService> services = new ConcurrentHashMap<>();
    private final OssServiceFactory ossServiceFactory;

    public AliyunOssServiceTenantAdapter(OssServiceFactory ossServiceFactory) {
        this.ossServiceFactory = ossServiceFactory;
    }

    @Override
    public OssService getTenantOssService() {
        return null;
    }

    @Override
    public boolean isBucketExist() {
        return false;
    }

    @Override
    public boolean createBucket() {
        return false;
    }

    @Override
    public boolean deleteBucket() {
        return false;
    }

    @Override
    public Bucket getBucket() {
        return null;
    }

    @Override
    public List<Bucket> listBuckets() {
        return List.of();
    }

    @Override
    public boolean setBucketAcl(AccessControlList acl) {
        return false;
    }

    @Override
    public AccessControlList getBucketAcl() {
        return null;
    }

    @Override
    public boolean setObjectAcl(String objectName, AccessControlList.ACL acl) {
        return false;
    }

    @Override
    public AccessControlList setObjectAcl(String objectName) {
        return null;
    }

    @Override
    public boolean setBucketPolicy(String policyJson) {
        return false;
    }

    @Override
    public String getBucketPolicy() {
        return "";
    }

    @Override
    public boolean putObject(String objectName, File file) {
        return false;
    }

    @Override
    public boolean putObject(String objectName, File file, ObjectMetadata metadata) {
        return false;
    }

    @Override
    public boolean putObject(String objectName, File file, AccessControlList.ACL acl) {
        return false;
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream) {
        return false;
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream, ObjectMetadata metadata) {
        return false;
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream, AccessControlList.ACL acl) {
        return false;
    }

    @Override
    public OssObject getObject(String objectName) {
        return null;
    }

    @Override
    public boolean deleteObject(String objectName) {
        return false;
    }

    @Override
    public boolean isObjectExist(String objectName) {
        return false;
    }

    @Override
    public ObjectMetadata getObjectMetadata(String objectName) {
        return null;
    }

    @Override
    public boolean copyObject(String sourceObjectName, String targetObjectName) {
        return false;
    }

    @Override
    public List<String> listObject() {
        return List.of();
    }

    @Override
    public List<String> listObject(int maximum) {
        return List.of();
    }

    @Override
    public List<String> listObject(String prefix) {
        return List.of();
    }

    @Override
    public List<String> listObject(String prefix, int maximum) {
        return List.of();
    }

    @Override
    public String getPresignedUrl(String objectName) {
        return "";
    }

    @Override
    public String getPresignedUrl(String objectName, int seconds) {
        return "";
    }

    @Override
    public String getPresignedUrl(String objectName, int seconds, String method) {
        return "";
    }

    @Override
    public String getUrl(String objectName) {
        return "";
    }

    @Override
    public String getObjectName(String url) {
        return "";
    }

    @Override
    public String getScaleImg(String url, int percentage) {
        return "";
    }

    @Override
    public String getUploadUrl(String objectName) {
        return "";
    }

    @Override
    public String getUploadUrl(String objectName, int seconds) {
        return "";
    }

    @Override
    public boolean uploadDirectory(String objectNamePrefix, File file) {
        return false;
    }

    @Override
    public boolean uploadDeepDirectory(String objectNamePrefix, File file) {
        return false;
    }

    @Override
    public boolean download(String objectName, File file) {
        return false;
    }
}
