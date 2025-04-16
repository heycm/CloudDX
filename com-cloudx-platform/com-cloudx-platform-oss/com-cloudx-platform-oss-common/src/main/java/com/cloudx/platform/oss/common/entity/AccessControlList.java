package com.cloudx.platform.oss.common.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 访问控制
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:18
 */
@Data
@Builder
public class AccessControlList {

    private AccessControlList.ACL acl;

    public enum ACL {
        Default, Private, PublicRead, PublicReadWrite, AuthenticatedRead, Unknown;
    }

}
