package com.augustxun.safe.controller.admin;


import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.vo.*;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
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
    public BaseResponse<String> addAccount(@RequestBody AccountAddRequest accountAddRequest,
                                           HttpServletRequest request) {
        return accountService.addAccountByAdmin(accountAddRequest, request);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @Operation(summary = "删除账户")
    @PostMapping("/delete")
    @Transactional
    public BaseResponse<Boolean> deleteAccount(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long acctNo = Long.parseLong(deleteRequest.getId());
        boolean b = accountService.deleteAccounts(acctNo);
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
    
    public BaseResponse<Boolean> updateAccount(@RequestBody AccountUpdateRequest accountUpdateRequest) {
        if (accountUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long acctNo = Long.parseLong(accountUpdateRequest.getAcctNo());
        if (acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return accountService.updateAccount(accountUpdateRequest);
    }

    // region 各类账户信息分页查询
    /**
     * 账户信息分页查询
     *
     * @param accountQueryRequest
     * @return
     */
    @Operation(summary = "Account表信息分页查询")
    @PostMapping("/list/page")
    
    public BaseResponse<Page<Account>> listAccountByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int size = accountQueryRequest.getPageSize();
        Page<Account> accountPage = accountService.page(new Page<>(current, size),
                accountService.getQueryWrapper(accountQueryRequest));
        return ResultUtils.success(accountPage);
    }

    @Operation(summary = "CheckingVO信息分页查询")
    @PostMapping("/list/checking/vo/page")
    public BaseResponse<Page<CheckingVO>> getCheckingVOByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<CheckingVO> checkingVOPage = accountService.listCheckingVOByPage(current, pageSize);
        return ResultUtils.success(checkingVOPage);
    }

    @Operation(summary = "SavingsVO信息分页查询")
    @PostMapping("/list/saving/vo/page")
    public BaseResponse<Page<SavingsVO>> getSavingsVOByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<SavingsVO> savingsVOPage = accountService.listSavingsVOByPage(current, pageSize);
        return ResultUtils.success(savingsVOPage);
    }

    @Operation(summary = "HomeLoanVO信息分页查询")
    @PostMapping("/list/home/loan/vo/page")
    public BaseResponse<Page<HomeLoanVO>> getHomeLoanVOByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<HomeLoanVO> homeLoanVOPage = accountService.listHomeLoanVOByPage(current, pageSize);
        return ResultUtils.success(homeLoanVOPage);
    }

    @Operation(summary = "StudentLoanVO信息分页查询")
    @PostMapping("/list/student/loan/vo/page")
    public BaseResponse<Page<StudentLoanVO>> getStudentLoanVOByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<StudentLoanVO> studentLoanVOPage = accountService.listStudentLoanVOByPage(current, pageSize);
        return ResultUtils.success(studentLoanVOPage);
    }

    @Operation(summary = "PersonalLoanVO信息分页查询")
    @PostMapping("/list/personal/loan/vo/page")
    public BaseResponse<Page<PersonalLoanVO>> getPersonalLoanVOByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<PersonalLoanVO> checkingVOPage = accountService.listPersonalLoanVOByPage(current, pageSize);
        return ResultUtils.success(checkingVOPage);
    }

}
