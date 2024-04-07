package com.augustxun.safe.controller;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.LoginFormDTO;
import com.augustxun.safe.model.dto.user.UserRegisterRequest;
import com.augustxun.safe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/sendCode")
    public BaseResponse<String> send(@RequestParam("phone") String phone, HttpSession session) {
        return userService.sendCode(phone, session);
    }

    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody LoginFormDTO loginFormDTO, HttpSession session) {
        return userService.login(loginFormDTO, session);
    }


    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }


}
