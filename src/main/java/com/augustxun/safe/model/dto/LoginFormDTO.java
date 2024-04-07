package com.augustxun.safe.model.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    private String phone;
    private String code;
    /**
     *   登录密码
     */
    private String password;
}
