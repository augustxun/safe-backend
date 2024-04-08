package com.augustxun.safe.controller;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.user.UserLoginRequest;
import com.augustxun.safe.model.dto.user.UserRegisterRequest;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RestControllerAdvice(basePackages = "com.augustxun.safe,controller")
@RequestMapping("/user")
@Api(tags = "用户控制")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    /**
     * 发送手机验证码
     * @param phone
     * @param session
     * @return
     */
    @Operation(summary = "发送验证码接口")
    @GetMapping("/sendCode")
    public BaseResponse<String> send(@RequestParam("phone") String phone, HttpSession session) {
        if (phone == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.sendCode(phone, session);
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @Operation(summary = "用户注册接口")
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


    /**
     * 用户登陆
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Operation(summary = "账号登录接口")
    @PostMapping("/account/login")
    public BaseResponse<User> accountLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.accountLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Operation(summary = "验证码登录接口")
    @PostMapping("/mobile/login")
    BaseResponse<User> mobileLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userPhone= userLoginRequest.getUserPhone();
        String smsCode = userLoginRequest.getSmsCode();
        if (StringUtils.isAnyBlank(userPhone, smsCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.mobileLogin(userPhone, smsCode, request);
        return ResultUtils.success(user);
    }


    // region 增删改查

}
