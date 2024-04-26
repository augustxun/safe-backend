package com.augustxun.safe.controller;

import cn.hutool.core.util.StrUtil;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.user.PasswordUpdateRequest;
import com.augustxun.safe.model.dto.user.UserLoginRequest;
import com.augustxun.safe.model.dto.user.UserRegisterRequest;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.LoginUserVO;
import com.augustxun.safe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.augustxun.safe.service.impl.UserServiceImpl.SALT;

@RestController
@RequestMapping("/user")
@Api(tags = "登陆状态相关接口")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 发送手机验证码
     *
     * @param phone
     * @param session
     * @return
     */
    @Operation(summary = "发送验证码")
    @GetMapping("/send")
    public BaseResponse<String> send(@RequestParam("phone") String phone, HttpSession session) {
        if (phone == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.sendCode(phone, session);
    }

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @Operation(summary = "注册")
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
     * 账号密码登录登陆
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Operation(summary = "账号密码登录")
    @PostMapping("/account/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Operation(summary = "退出")
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
    @Operation(summary = "获取当前用户")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }


    @Operation(summary = "修改密码")
    @PostMapping("/update/pwd")
    public BaseResponse<String> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest, HttpServletRequest request) {
        if (passwordUpdateRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "修改失败");
        }
        String oldPassword = passwordUpdateRequest.getOldPassword(); // 旧密码
        String newPassword = passwordUpdateRequest.getNewPassword(); // 新密码
        String checkPassword = passwordUpdateRequest.getCheckPassword(); // 用于核对的密码
        if (StrUtil.isBlank(oldPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "旧密码不可为空");
        }
        if (StrUtil.isBlank(newPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "新密码不可为空");
        }
        if (StrUtil.isBlank(checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请再次确认新密码");
        }
        User loginUser = userService.getLoginUser(request);
        long id = loginUser.getId();
        User user = userService.getById(id);
        String encryptOldPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        if (!encryptOldPassword.equals(user.getUserPassword())) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "旧密码错误");
        }
        if (!newPassword.equals((checkPassword))) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "两次输入密码不一致");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex(((SALT + newPassword).getBytes()));
        user.setUserPassword(encryptPassword);
        userService.updateById(user);
        return ResultUtils.success("密码修改成功");
    }
}
