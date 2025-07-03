package com.cloudx.platform.domain.model;

/**
 * DP，领域中最基本的类型、值对象，是无状态的、不可变的，包含值和行为，关注数据类型和数据值的含义和约束 <br/>
 * 一般用作值对象，如：用户名、手机号、邮箱、地址、金额、IP、URL、密码、身份证号、银行卡号、车牌号、邮政编码、经纬度等 <br/>
 * 仅限有参构造方法创建对象并完成数据值检查，不允许无参构造方法创建对象，当一个DP被成功创建时，它将是完全合法有效的对象
 * @author heycm
 * @version 1.0
 * @since 2025/6/28 20:47
 */
public interface DomainPrimitive extends ValueObject {}
