package com.augustxun.safe.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *

 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户账户名
     */
    private String userAccount;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Date createTime;
    /**
     * 用户名
     */
    CustomerVO customerVO;
    private static final long serialVersionUID = 1L;
}