package com.augustxun.safe.model.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 */
@Data
@ApiModel
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: Customer, admin
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;
    /**
     * 手机号
     */
    private String userPhone;
    private static final long serialVersionUID = 1L;
}