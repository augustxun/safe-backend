package com.augustxun.safe.controller.user;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.user.UserUpdateRequest;
import com.augustxun.safe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("user/user")
@Api(tags = "C端-用户管理接口")
public class UserUserController {
    @Resource
    private UserService userService;

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @Operation(summary = "更新用户接口")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userUpdateRequest);
    }

}
