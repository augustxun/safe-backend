package com.augustxun.safe.controller.user;

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
import com.augustxun.safe.model.entity.*;
import com.augustxun.safe.model.vo.AccountVO;
import com.augustxun.safe.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
@Api(tags = "C端-账户接口")
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
     * 创建
     *
     * @param accountAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "创建账户接口（用户/管理员）")
    @PostMapping("/add")
    public BaseResponse<String> addAccount(@RequestBody AccountAddRequest accountAddRequest, HttpServletRequest request) {
        if (accountAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.校验参数是否合规
        Account account = new Account();
        BeanUtils.copyProperties(accountAddRequest, account);
        accountService.validAccount(account, true);
        // 2.校验该用户是否已经填写个人信息
        User loginUser = userService.getLoginUser(request);
        Long userCustomerId = loginUser.getCustomerId();
        if (userCustomerId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "失败，请先填写个人信息");
        }
        String type = account.getType();
        if (type.equals("C")) {
            Checking checking = checkingService.getOne(new QueryWrapper<Checking>().eq("customerId", userCustomerId));
            if (checking != null) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "每个用户最多一个 Checking 账户");
            }
            boolean result = accountService.save(account);
            Long newAccountId = accountService.getOne(new QueryWrapper<Account>().eq("userId", loginUser.getId()).eq("type", type)).getAcctNo();
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            checking.setAcctNo(newAccountId);
            checking.setServiceFee(new BigDecimal("300.00"));
            checking.setBalance(new BigDecimal("0"));
            checking.setCustomerId(userCustomerId);
            result |= checkingService.save(checking);
        } else if (type.equals("S")) {

            Savings savings = savingsService.getOne(new QueryWrapper<Savings>().eq("customerId", userCustomerId));
            if (savings != null) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "每个用户最多一个 Savings 账户");
            }
            boolean result = accountService.save(account);
            Long newAccountId = accountService.getOne(new QueryWrapper<Account>().eq("userId", loginUser.getId()).eq("type", type)).getAcctNo();
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            savings.setAcctNo(newAccountId);
            savings.setBalance(new BigDecimal("0"));
            savings.setInterestRate(new BigDecimal("0.05"));
            savings.setCustomerId(userCustomerId);
            result |= savingsService.save(savings);
        } else if (type.equals("L")) {
            if (!loginUser.getUserRole().equals("admin")) {
                return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "没有创建权限，请向管理员申请");
            }
            Loan loan = new Loan();

        }

        Long customerId = userCustomerId;


        return ResultUtils.success("创建成功");
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @Operation(summary = "删除账户接口（用户/管理员）")
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
    @Operation(summary = "更新账户接口（用户/管理员）")
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
     * 分页获取列表（仅管理员）
     *
     * @param accountQueryRequest
     * @return
     */
    @Operation(summary = "分页获取账户列表（仅管理员）")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Account>> listAccountByPage(@RequestBody AccountQueryRequest accountQueryRequest) {
        long current = accountQueryRequest.getCurrent();
        long size = accountQueryRequest.getPageSize();
        Page<Account> customerPage = accountService.page(new Page<>(current, size), accountService.getQueryWrapper(accountQueryRequest));
        return ResultUtils.success(customerPage);
    }

    /**
     * 根据当前 loginUser 获取分页列表（用户使用）
     *
     * @param accountQueryRequest
     * @return
     */
    @Operation(summary = "根据当前用户 ID 分页获取账户列表")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<AccountVO>> listAccountVOByPage(@RequestBody AccountQueryRequest accountQueryRequest, HttpServletRequest httpServletRequest) {
        Long id = accountQueryRequest.getUserId();
        if (!id.equals(userService.getLoginUser(httpServletRequest).getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = accountQueryRequest.getCurrent();
        long size = accountQueryRequest.getPageSize();
        Page<Account> customerPage = accountService.page(new Page<>(current, size), accountService.getQueryWrapper(accountQueryRequest));
        return ResultUtils.success(accountService.getAccountVOPage(customerPage));
    }
}
