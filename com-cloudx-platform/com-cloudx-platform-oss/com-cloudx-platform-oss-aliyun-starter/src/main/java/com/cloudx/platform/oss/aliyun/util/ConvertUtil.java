package com.cloudx.platform.oss.aliyun.util;

import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectPermission;
import com.cloudx.platform.oss.common.entity.AccessControlList;

/**
 * 转换类
 * @author heycm
 * @version 1.0
 * @since 2025/4/17 15:52
 */
public class ConvertUtil {

    public static AccessControlList.ACL convert(CannedAccessControlList cacl) {
        return switch (cacl) {
            case Default -> AccessControlList.ACL.Default;
            case Private -> AccessControlList.ACL.Private;
            case PublicRead -> AccessControlList.ACL.PublicRead;
            case PublicReadWrite -> AccessControlList.ACL.PublicReadWrite;
            case AuthenticatedRead -> AccessControlList.ACL.AuthenticatedRead;
            default -> AccessControlList.ACL.Unknown;
        };
    }

    public static CannedAccessControlList convert(AccessControlList.ACL cacl) {
        return switch (cacl) {
            case Default -> CannedAccessControlList.Default;
            case Private -> CannedAccessControlList.Private;
            case PublicRead -> CannedAccessControlList.PublicRead;
            case PublicReadWrite -> CannedAccessControlList.PublicReadWrite;
            case AuthenticatedRead -> CannedAccessControlList.AuthenticatedRead;
            default -> CannedAccessControlList.Unknown;
        };
    }

    public static AccessControlList.ACL convert(ObjectPermission permission) {
        return switch (permission) {
            case Default -> AccessControlList.ACL.Default;
            case Private -> AccessControlList.ACL.Private;
            case PublicRead -> AccessControlList.ACL.PublicRead;
            case PublicReadWrite -> AccessControlList.ACL.PublicReadWrite;
            default -> AccessControlList.ACL.Unknown;
        };
    }
}
