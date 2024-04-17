package com.augustxun.safe.controller;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.user.UserLoginRequest;
import com.augustxun.safe.model.dto.user.UserRegisterRequest;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.LoginUserVO;
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
@Api(tags = "UserController")
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
    @GetMapping("/send")
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
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        System.err.println(userAccount);
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }



    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Operation(summary = "用户退出接口")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Operation(summary = "获取当前登录用户接口")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // region 增删改查

}
