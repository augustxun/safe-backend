package com.augustxun.safe.controller;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/account")
@Api(tags = "AccountController")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param accountAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "创建账户接口")
    @PostMapping("/add")
    public BaseResponse<Long> addAccount(@RequestBody AccountAddRequest accountAddRequest, HttpServletRequest request) {
        if (accountAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Account account = new Account();
        BeanUtils.copyProperties(accountAddRequest, account);
        // 校验
        accountService.validAccount(account, true);
        // TODO:加入用户的 userId
        User loginUser = userService.getLoginUser(request);
        account.setUserId(loginUser.getId());
        boolean result = accountService.save(account);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newAccountId = account.getAcctNo();
        return ResultUtils.success(newAccountId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAccount(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Account oldAccount = accountService.getById(id);
        ThrowUtils.throwIf(oldAccount == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldAccount.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = accountService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param accountUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAccount(@RequestBody AccountUpdateRequest accountUpdateRequest) {
        if (accountUpdateRequest == null || accountUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Account account = new Account();
        BeanUtils.copyProperties(accountUpdateRequest, account);
        // 参数校验
        accountService.validAccount(account, false);
        long id = accountUpdateRequest.getId();
        // 判断是否存在
        Account oldAccount = accountService.getById(id);
        ThrowUtils.throwIf(oldAccount == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = accountService.updateById(account);
        return ResultUtils.success(result);
    }
}
