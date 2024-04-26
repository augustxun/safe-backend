package com.augustxun.safe.controller.admin;


import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController("AdminAccountController")
@RequestMapping("admin/account")
@Api(tags = "B端-账户管理接口")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;
    @Resource
    private CustomerService customerService;
    @Resource
    private CheckingService checkingService;

    @Resource
    private SavingsService savingsService;

    @Resource
    private LoanService loanService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 管理员端新建账户
     *
     * @param accountAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "新建账户")
    @PostMapping("/add")
    @Transactional
    public BaseResponse<String> addAccount(@RequestBody AccountAddRequest accountAddRequest, HttpServletRequest request) {
        if (accountAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.校验参数是否合规
        Account account = new Account();
        BeanUtils.copyProperties(accountAddRequest, account);
        accountService.validAccount(account, true);
        // 2.添加账户
        String type = account.getType(); // 账户类型
        Long userId = userService.getLoginUser(request).getId(); // 账户 userId
        // 3.检查该类型账户是否已经被创建
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserId, userId).eq(Account::getType, type);
        Account accountServiceOne = accountService.getOne(queryWrapper);
        // 3.1 已被创建，返回失败
        if (accountServiceOne != null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "该用户已有" + type + "类账户，请勿重复创建");
        }
        // 3.2 未被创建，创建账户

        accountService.save(account); // 保存账户信息到 account 表
        Long newAccountNo = accountService.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", "C")).getAcctNo();
        if (type.equals("C")) {
            return checkingService.addCheckingAccount(newAccountNo);
        } else if (type.equals("S")) {
            return savingsService.addSavingsAccount(newAccountNo);
        } else {
            return loanService.addLoanAccount(newAccountNo);
        }
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @Operation(summary = "删除账户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAccount(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        long id = Long.parseLong(deleteRequest.getId());
        if (deleteRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
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
     * 更新
     *
     * @param accountUpdateRequest
     * @return
     */
    @Operation(summary = "更新账户")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAccount(@RequestBody AccountUpdateRequest accountUpdateRequest) {
        long acctNo = Long.parseLong(accountUpdateRequest.getAcctNo());

        if (accountUpdateRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Account account = new Account();
        BeanUtils.copyProperties(accountUpdateRequest, account);
        account.setAcctNo(acctNo);
        // 参数校验
        accountService.validAccount(account, false);
        // 判断是否存在
        Account oldAccount = accountService.getById(acctNo);
        ThrowUtils.throwIf(oldAccount == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = accountService.updateById(account);
        return ResultUtils.success(result);
    }

    /**
     * 账户信息分页查询
     *
     * @param accountQueryRequest
     * @return
     */
    @Operation(summary = "账户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Account>> listAccountByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        long current = accountQueryRequest.getCurrent();
        long size = accountQueryRequest.getPageSize();
        Page<Account> accountPage = accountService.page(new Page<>(current, size), accountService.getQueryWrapper(accountQueryRequest));
        return ResultUtils.success(accountPage);
    }

}
