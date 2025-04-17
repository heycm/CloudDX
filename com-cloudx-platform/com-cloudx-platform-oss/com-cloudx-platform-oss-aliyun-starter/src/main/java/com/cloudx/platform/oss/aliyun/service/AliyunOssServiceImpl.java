package com.cloudx.platform.oss.aliyun.service;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetBucketPolicyResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectAcl;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import com.cloudx.platform.oss.aliyun.util.ConvertUtil;
import com.cloudx.platform.oss.common.entity.AccessControlList;
import com.cloudx.platform.oss.common.entity.AccessControlList.ACL;
import com.cloudx.platform.oss.common.entity.Bucket;
import com.cloudx.platform.oss.common.entity.ObjectMetadata;
import com.cloudx.platform.oss.common.entity.OssObject;
import com.cloudx.platform.oss.common.properties.OssProperties;
import com.cloudx.platform.oss.common.service.OssService;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云OSS服务实现
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 15:56
 */
@Slf4j
public class AliyunOssServiceImpl implements OssService {

    private final OSS ossClient;
    private final OssProperties properties;
    private final String bucketName;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC);

    public AliyunOssServiceImpl(OSS ossClient, OssProperties properties) {
        this.ossClient = ossClient;
        this.properties = properties;
        this.bucketName = properties.getBucketName();
    }

    @Override
    public boolean isBucketExist() {
        return operateBool(() -> ossClient.doesBucketExist(bucketName));
    }

    @Override
    public boolean createBucket() {
        if (isBucketExist()) {
            return true;
        }
        return operateBool(() -> {
            CreateBucketRequest request = new CreateBucketRequest(bucketName);
            ossClient.createBucket(request);
            return true;
        });
    }

    @Override
    public boolean deleteBucket() {
        return operateBool(() -> {
            ossClient.deleteBucket(bucketName);
            return true;
        });
    }

    @Override
    public Bucket getBucket() {
        return operate(() -> {
            BucketInfo info = ossClient.getBucketInfo(bucketName);
            return Bucket.builder().name(info.getBucket().getName()).creationDate(info.getBucket().getCreationDate()).build();
        });
    }

    @Override
    public List<Bucket> listBuckets() {
        return operate(() -> {
            List<com.aliyun.oss.model.Bucket> buckets = ossClient.listBuckets();
            return buckets.stream().map(e -> Bucket.builder().name(e.getName()).creationDate(e.getCreationDate()).build())
                    .collect(Collectors.toList());
        });
    }

    @Override
    public boolean setBucketAcl(AccessControlList acl) {
        return operateBool(() -> {
            ossClient.setBucketAcl(bucketName, ConvertUtil.convert(acl.getAcl()));
            return true;
        });
    }

    @Override
    public AccessControlList getBucketAcl() {
        return operate(() -> {
            com.aliyun.oss.model.AccessControlList bucketAcl = ossClient.getBucketAcl(bucketName);
            return AccessControlList.builder().acl(ConvertUtil.convert(bucketAcl.getCannedACL())).build();
        });
    }

    @Override
    public boolean setObjectAcl(String objectName, ACL acl) {
        return operateBool(() -> {
            ossClient.setObjectAcl(bucketName, objectName, ConvertUtil.convert(acl));
            return true;
        });
    }

    @Override
    public AccessControlList setObjectAcl(String objectName) {
        return operate(() -> {
            ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, objectName);
            return AccessControlList.builder().acl(ConvertUtil.convert(objectAcl.getPermission())).build();
        });
    }

    @Override
    public boolean setBucketPolicy(String policyJson) {
        return operateBool(() -> {
            ossClient.setBucketPolicy(bucketName, policyJson);
            return true;
        });
    }

    @Override
    public String getBucketPolicy() {
        return operate(() -> {
            GetBucketPolicyResult result = ossClient.getBucketPolicy(bucketName);
            return result.getPolicyText();
        });
    }

    @Override
    public boolean putObject(String objectName, File file) {
        return operateBool(() -> {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, file);
            ossClient.putObject(request);
            return true;
        });
    }

    @Override
    public boolean putObject(String objectName, File file, ObjectMetadata metadata) {
        return operateBool(() -> {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, file);
            com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
            objectMetadata.setUserMetadata(metadata.getUserMetadata());
            metadata.getRawMetadata().forEach(objectMetadata::setHeader);
            request.setMetadata(objectMetadata);
            ossClient.putObject(request);
            return true;
        });
    }

    @Override
    public boolean putObject(String objectName, File file, ACL acl) {
        return operateBool(() -> {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, file);
            com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
            objectMetadata.setObjectAcl(ConvertUtil.convert(acl));
            request.setMetadata(objectMetadata);
            ossClient.putObject(request);
            return true;
        });
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream) {
        return operateBool(() -> {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, inputStream);
            ossClient.putObject(request);
            return true;
        });
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream, ObjectMetadata metadata) {
        return operateBool(() -> {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, inputStream);
            com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
            objectMetadata.setUserMetadata(metadata.getUserMetadata());
            metadata.getRawMetadata().forEach(objectMetadata::setHeader);
            request.setMetadata(objectMetadata);
            ossClient.putObject(request);
            return true;
        });
    }

    @Override
    public boolean putObject(String objectName, InputStream inputStream, ACL acl) {
        return operateBool(() -> {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, inputStream);
            com.aliyun.oss.model.ObjectMetadata objectMetadata = new com.aliyun.oss.model.ObjectMetadata();
            objectMetadata.setObjectAcl(ConvertUtil.convert(acl));
            request.setMetadata(objectMetadata);
            ossClient.putObject(request);
            return true;
        });
    }

    @Override
    public OssObject getObject(String objectName) {
        return operate(() -> {
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            OssObject object = new OssObject();
            object.setName(ossObject.getKey());
            object.setBucketName(ossObject.getBucketName());
            object.setObjectContent(ossObject.getObjectContent());
            com.aliyun.oss.model.ObjectMetadata objectMetadata = ossObject.getObjectMetadata();
            objectMetadata.getUserMetadata().forEach((k, v) -> object.getMetadata().addUserMetadata(k, v));
            objectMetadata.getRawMetadata().forEach((k, v) -> object.getMetadata().addRowMetadata(k, v));
            return object;
        });
    }

    @Override
    public boolean deleteObject(String objectName) {
        return operateBool(() -> {
            ossClient.deleteObject(bucketName, objectName);
            return true;
        });
    }

    @Override
    public boolean isObjectExist(String objectName) {
        return operateBool(() -> ossClient.doesObjectExist(bucketName, objectName));
    }

    @Override
    public ObjectMetadata getObjectMetadata(String objectName) {
        return operate(() -> {
            com.aliyun.oss.model.ObjectMetadata objectMetadata = ossClient.getObjectMetadata(bucketName, objectName);
            ObjectMetadata metadata = new ObjectMetadata();
            objectMetadata.getRawMetadata().forEach(metadata::addRowMetadata);
            objectMetadata.getUserMetadata().forEach(metadata::addUserMetadata);
            return metadata;
        });
    }

    @Override
    public boolean copyObject(String sourceObjectName, String targetObjectName) {
        return operateBool(() -> {
            ossClient.copyObject(sourceObjectName, bucketName, targetObjectName, bucketName);
            return true;
        });
    }

    @Override
    public List<String> listObject() {
        return operate(() -> {
            ListObjectsV2Result result = ossClient.listObjectsV2(bucketName);
            return result.getObjectSummaries().stream().map(OSSObjectSummary::getKey).toList();
        });
    }

    @Override
    public List<String> listObject(int maximum) {
        return operate(() -> {
            ObjectListing listing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMaxKeys(maximum));
            return listing.getObjectSummaries().stream().map(OSSObjectSummary::getKey).toList();
        });
    }

    @Override
    public List<String> listObject(String prefix) {
        return operate(() -> {
            ListObjectsV2Result result = ossClient.listObjectsV2(bucketName, prefix);
            return result.getObjectSummaries().stream().map(OSSObjectSummary::getKey).toList();
        });
    }

    @Override
    public List<String> listObject(String prefix, int maximum) {
        return operate(() -> {
            ObjectListing listing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(prefix).withMaxKeys(maximum));
            return listing.getObjectSummaries().stream().map(OSSObjectSummary::getKey).toList();
        });
    }

    @Override
    public String getPresignedUrl(String objectName) {
        return getPresignedUrl(objectName, 604800);
    }

    @Override
    public String getPresignedUrl(String objectName, int seconds) {
        return getPresignedUrl(objectName, seconds, "GET");
    }

    @Override
    public String getPresignedUrl(String objectName, int seconds, String method) {
        return operate(() -> {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
            HttpMethod httpMethod;
            if ("GET".equalsIgnoreCase(method)) {
                httpMethod = HttpMethod.GET;
            } else if ("POST".equalsIgnoreCase(method)) {
                httpMethod = HttpMethod.POST;
            } else if ("DELETE".equalsIgnoreCase(method)) {
                httpMethod = HttpMethod.DELETE;
            } else if ("PUT".equalsIgnoreCase(method)) {
                httpMethod = HttpMethod.PUT;
            } else if ("OPTIONS".equalsIgnoreCase(method)) {
                httpMethod = HttpMethod.OPTIONS;
            } else if ("HEAD".equalsIgnoreCase(method)) {
                httpMethod = HttpMethod.HEAD;
            } else {
                httpMethod = HttpMethod.GET;
            }
            request.setMethod(httpMethod);
            request.setExpiration(new Date(new Date().getTime() + seconds * 1000L));
            URL url = ossClient.generatePresignedUrl(request);
            return url.toString();
        });
    }

    @Override
    public String getUrl(String objectName) {
        return operate(
                () -> properties.getProtocol() + "://" + properties.getBucketName() + '.' + properties.getEndpoint() + '/' + objectName);
    }

    @Override
    public String getObjectName(String url) {
        return operate(() -> {
            String base = url.split("\\?")[0];
            return base.replace((properties.getProtocol() + "://" + properties.getBucketName() + '.' + properties.getEndpoint() + '/'), "");
        });
    }

    @Override
    public String getScaleImg(String url, int percentage) {
        if (percentage == 100) {
            return url;
        }
        percentage = Math.max(percentage, 1);
        percentage = Math.min(percentage, 1000);
        int p = percentage;
        return operate(() -> {
            if (url.contains("x-oss-signature")) {
                String objectName = getObjectName(url);
                String[] params = url.split("\\?")[1].split("&");
                ZonedDateTime signTime = null;
                int expire = -1;
                for (String param : params) {
                    if (signTime != null && expire != -1) {
                        break;
                    }
                    if (signTime == null && param.contains("x-oss-date")) {
                        signTime = ZonedDateTime.parse(param.substring(param.lastIndexOf("=") + 1), FORMATTER)
                                .withZoneSameInstant(ZoneId.of("GMT+8"));
                        continue;
                    }
                    if (expire == -1 && param.contains("x-oss-expires")) {
                        expire = Integer.parseInt(param.substring(param.lastIndexOf("=") + 1));
                    }
                }
                signTime = signTime.plusSeconds(expire);
                GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
                req.setExpiration(Date.from(signTime.toInstant()));
                req.setProcess("image/resize,p_" + p);
                return ossClient.generatePresignedUrl(req).toString();
            }
            return url + "?x-oss-process=image/resize,p_" + p;
        });
    }

    @Override
    public String getUploadUrl(String objectName) {
        return getUploadUrl(objectName, 604800);
    }

    @Override
    public String getUploadUrl(String objectName, int seconds) {
        return operate(() -> {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.PUT);
            request.setExpiration(new Date(new Date().getTime() + seconds * 1000L));
            URL url = ossClient.generatePresignedUrl(request);
            return url.toString();
        });
    }

    @Override
    public boolean uploadDirectory(String objectNamePrefix, File file) {
        log.warn("Aliyun OSS not support upload directory.");
        return false;
    }

    @Override
    public boolean uploadDeepDirectory(String objectNamePrefix, File file) {
        log.warn("Aliyun OSS not support upload directory.");
        return false;
    }

    @Override
    public boolean download(String objectName, File file) {
        return operateBool(() -> {
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), file);
            return true;
        });
    }

    private boolean operateBool(Supplier<Boolean> supplier) {
        try {
            return Boolean.TRUE.equals(supplier.get());
        } catch (Exception e) {
            log.error("Aliyun OSS operate error.", e);
        }
        return false;
    }

    private <T> T operate(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("Aliyun OSS operate error.", e);
        }
        return null;
    }
}
