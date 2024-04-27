package com.augustxun.safe.model.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class PasswordUpdateRequest {
    String oldPassword;
    String newPassword;
    String checkPassword;
}
