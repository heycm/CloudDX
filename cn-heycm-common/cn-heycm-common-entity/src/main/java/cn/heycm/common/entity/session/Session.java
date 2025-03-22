package cn.heycm.common.entity.session;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户 Session
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 20:14
 */
@Data
public class Session implements Serializable {

    @Serial
    private static final long serialVersionUID = 2610169595436428693L;

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 授权角色
     */
    private int roleId;

    /**
     * 手机号
     */
    private String phoneId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 用户状态
     */
    private boolean userStatus;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * Token
     */
    private String token;

    /**
     * 登录时间
     */
    private Date loginTime;
}
