package com.augustxun.safe.model.dto.user;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    String oldPassword;
    String newPassword;
    String checkPassword;
}
