package com.augustxun.safe.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.*;
import com.augustxun.safe.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.augustxun.safe.constant.AccountConstants.*;

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
    @Transactional
    public BaseResponse<String> addAccount(@RequestBody AccountAddRequest accountAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登陆");
        }
        if (accountAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.校验参数是否合规
        Account account = new Account();
        BeanUtils.copyProperties(accountAddRequest, account);
        accountService.validAccount(account, true);
        // 2.创建账户
        String type = account.getType(); // 账户类型
        // 2.1 检查该类型账户是否已经被创建
        Long userId = Long.parseLong(accountAddRequest.getUserId());

        if (userId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请指定账户所属用户");
        }
        User user = userService.getById(userId);
        if (user == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserId, userId).eq(Account::getType, type);
        Account accountServiceOne = accountService.getOne(queryWrapper);
        // 已被创建，操作失败
        if (accountServiceOne != null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "该用户已有" + type + "类账户，请勿重复创建");
        }
        // 未被创建，创建账户
        account.setUserId(userId); // 保存指定的用户 UserId 到 Account 中
        accountService.save(account); // 保存账户信息到 account 表
        Long newAccountNo =
                accountService.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", type)).getAcctNo();
        return accountService.saveAccounts(newAccountNo, type, accountAddRequest);
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
        boolean b = accountService.deleteAccounts(deleteRequest);
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
    @Operation(summary = "Account表信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Account>> listAccountByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        int current = accountQueryRequest.getCurrent();
        int size = accountQueryRequest.getPageSize();
        Page<Account> accountPage = accountService.page(new Page<>(current, size), accountService.getQueryWrapper(accountQueryRequest));
        return ResultUtils.success(accountPage);
    }
    @Operation(summary = "CheckingVO信息分页查询")
    @PostMapping("/list/checking/vo/page")
    public BaseResponse<Page<CheckingVO>> getCheckingVOByPage(@RequestBody AccountQueryRequest accountQueryRequest){
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<CheckingVO> checkingVOPage = accountService.listCheckingVOByPage(current, pageSize);
        return ResultUtils.success(checkingVOPage);
    }
    @Operation(summary = "SavingsVO信息分页查询")
    @PostMapping("/list/saving/vo/page")
    public BaseResponse<Page<SavingsVO>> getSavingsVOByPage(@RequestBody AccountQueryRequest accountQueryRequest){
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<SavingsVO> savingsVOPage = accountService.listSavingsVOByPage(current, pageSize);
        return ResultUtils.success(savingsVOPage);
    }
    @Operation(summary = "HomeLoanVO信息分页查询")
    @PostMapping("/list/home/loan/vo/page")
    public BaseResponse<Page<HomeLoanVO>> getHomeLoanVOByPage(@RequestBody AccountQueryRequest accountQueryRequest){
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<HomeLoanVO> homeLoanVOPage = accountService.listHomeLoanVOByPage(current, pageSize);
        return ResultUtils.success(homeLoanVOPage);
    }
    @Operation(summary = "StudentLoanVO信息分页查询")
    @PostMapping("/list/student/loan/vo/page")
    public BaseResponse<Page<StudentLoanVO>> getStudentLoanVOByPage(@RequestBody AccountQueryRequest accountQueryRequest){
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<StudentLoanVO> studentLoanVOPage = accountService.listStudentLoanVOByPage(current, pageSize);
        return ResultUtils.success(studentLoanVOPage);
    }
    @Operation(summary = "PersonalLoanVO信息分页查询")
    @PostMapping("/list/personal/loan/vo/page")
    public BaseResponse<Page<PersonalLoanVO>> getPersonalLoanVOByPage(@RequestBody AccountQueryRequest accountQueryRequest){
        int current = accountQueryRequest.getCurrent();
        int pageSize = accountQueryRequest.getPageSize();
        Page<PersonalLoanVO> checkingVOPage = accountService.listPersonalLoanVOByPage(current, pageSize);
        return ResultUtils.success(checkingVOPage);
    }

}
