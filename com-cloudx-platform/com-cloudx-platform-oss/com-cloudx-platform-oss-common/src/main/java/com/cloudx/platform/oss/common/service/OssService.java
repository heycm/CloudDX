package com.cloudx.platform.oss.common.service;

import com.cloudx.platform.oss.common.entity.AccessControlList;
import com.cloudx.platform.oss.common.entity.Bucket;
import com.cloudx.platform.oss.common.entity.ObjectMetadata;
import com.cloudx.platform.oss.common.entity.OssObject;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * OSS操作接口
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:26
 */
public interface OssService {

    /**
     * 判断bucket是否存在
     * @return true:存在 false:不存在
     */
    boolean isBucketExist();

    /**
     * 创建bucket
     * @return true:创建成功 false:创建失败
     */
    boolean createBucket();

    /**
     * 删除bucket
     * @return true:删除成功 false:删除失败
     */
    boolean deleteBucket();

    /**
     * 获取bucket
     * @return bucket
     */
    Bucket getBucket();

    /**
     * 获取bucket列表
     * @return bucket列表
     */
    List<Bucket> listBuckets();

    /**
     * 设置bucket权限
     * @param acl 权限
     * @return true:设置成功 false:设置失败
     */
    boolean setBucketAcl(AccessControlList acl);

    /**
     * 获取bucket权限
     * @return 权限
     */
    AccessControlList getBucketAcl();

    /**
     * 设置文件对象权限
     * @param acl 权限
     * @return true:设置成功 false:设置失败
     */
    boolean setObjectAcl(String objectName, AccessControlList.ACL acl);

    /**
     * 获取文件对象权限
     * @param objectName 文件
     * @return 权限
     */
    AccessControlList setObjectAcl(String objectName);

    /**
     * 设置Bucket授权策略
     * @param policyJson JSON格式字符串
     * @return true:设置成功 false:设置失败
     */
    boolean setBucketPolicy(String policyJson);

    /**
     * 获取Bucket授权策略
     * @return JSON
     */
    String getBucketPolicy();

    /**
     * 上传文件对象
     * @param objectName 对象名称
     * @param file       文件
     * @return true:上传成功 false:上传失败
     */
    boolean putObject(String objectName, File file);

    /**
     * 上传文件对象
     * @param objectName 对象名称
     * @param file       文件
     * @param metadata   元数据
     * @return true:上传成功 false:上传失败
     */
    boolean putObject(String objectName, File file, ObjectMetadata metadata);

    /**
     * 上传文件对象
     * @param objectName 对象名称
     * @param file       文件
     * @param acl        访问权限
     * @return true:上传成功 false:上传失败
     */
    boolean putObject(String objectName, File file, AccessControlList.ACL acl);

    /**
     * 上传流对象
     * @param objectName  对象名称
     * @param inputStream 流数据
     * @return true:上传成功 false:上传失败
     */
    boolean putObject(String objectName, InputStream inputStream);

    /**
     * 上传流对象
     * @param objectName  对象名称
     * @param inputStream 流数据
     * @param metadata    元数据
     * @return true:上传成功 false:上传失败
     */
    boolean putObject(String objectName, InputStream inputStream, ObjectMetadata metadata);

    /**
     * 上传流对象
     * @param objectName  对象名称
     * @param inputStream 流数据
     * @param acl         访问权限
     * @return true:上传成功 false:上传失败
     */
    boolean putObject(String objectName, InputStream inputStream, AccessControlList.ACL acl);

    /**
     * 获取对象
     * @param objectName 对象名称
     * @return 对象 InputStream
     */
    OssObject getObject(String objectName);

    /**
     * 删除对象
     * @param objectName 对象名称
     * @return true:删除成功 false:删除失败
     */
    boolean deleteObject(String objectName);

    /**
     * 判断对象是否存在
     * @param objectName 对象名称
     * @return true:存在 false:不存在
     */
    boolean isObjectExist(String objectName);

    /**
     * 获取对象元数据
     * @param objectName 对象名称
     * @return 对象元数据
     */
    ObjectMetadata getObjectMetadata(String objectName);

    /**
     * 复制对象
     * @param sourceObjectName 源对象名称
     * @param targetObjectName 目标对象名称
     * @return true:复制成功 false:复制失败
     */
    boolean copyObject(String sourceObjectName, String targetObjectName);

    /**
     * 获取对象列表
     * @return 对象列表
     */
    List<String> listObject();

    /**
     * 获取对象列表
     * @param maximum 最大数量
     * @return 对象列表
     */
    List<String> listObject(int maximum);

    /**
     * 获取对象列表
     * @param prefix 前缀
     * @return 对象列表
     */
    List<String> listObject(String prefix);

    /**
     * 获取对象列表
     * @param prefix  前缀
     * @param maximum 最大数量
     * @return 对象列表
     */
    List<String> listObject(String prefix, int maximum);

    /**
     * 获取预签名URL，7天有效
     * @param objectName 对象名称
     * @return URL
     */
    String getPresignedUrl(String objectName);

    /**
     * 获取预签名URL
     * @param objectName 对象名称
     * @param seconds    有效时间（最大7天）
     * @return URL
     */
    String getPresignedUrl(String objectName, int seconds);

    /**
     * 获取预签名URL
     * @param objectName 对象名称
     * @param seconds    有效时间（最大7天）
     * @param method     请求方式
     * @return URL
     */
    String getPresignedUrl(String objectName, int seconds, String method);

    /**
     * 获取URL
     * @param objectName 对象名称
     * @return URL
     */
    String getUrl(String objectName);

    /**
     * 获取URL中文件对象名称
     * @param url 文件对象URL
     * @return objectName 对象名称
     */
    String getObjectName(String url);

    /**
     * 图片等比缩放
     * @param url        图片URL
     * @param percentage 缩放比例：缩小[1,100] 放大[100,1000]
     * @return 处理后的图片地址
     */
    String getScaleImg(String url, int percentage);

    /**
     * 获取签名上传URL，7天有效
     * @param objectName 对象名称
     * @return URL
     */
    String getUploadUrl(String objectName);

    /**
     * 获取签名上传URL
     * @param objectName 对象名称
     * @param seconds    有效时间（最大7天）
     * @return URL
     */
    String getUploadUrl(String objectName, int seconds);

    /**
     * 上传目录，文件名即为对象名
     * @param objectNamePrefix 对象名称前缀
     * @param file             目录
     * @return 是否成功
     */
    boolean uploadDirectory(String objectNamePrefix, File file);

    /**
     * 上传目录，文件名即为对象名（递归子目录）
     * @param objectNamePrefix 对象名称前缀
     * @param file             目录
     * @return 是否成功
     */
    boolean uploadDeepDirectory(String objectNamePrefix, File file);

    /**
     * 下载文件
     * @param objectName 对象名称
     * @param file       文件
     * @return 是否成功
     */
    boolean download(String objectName, File file);

}
