package com.augustxun.safe.controller.user;

import cn.hutool.core.util.StrUtil;
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
import com.augustxun.safe.model.vo.*;
import com.augustxun.safe.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.augustxun.safe.constant.AccountConstants.*;

@RestController
@RequestMapping("user/account")
@Api(tags = "C端-账户管理接口")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;
    @Resource
    private CheckingService checkingService;

    @Resource
    private SavingsService savingsService;

    @Resource
    private LoanService loanService;
    @Resource
    private PersonalService personalService;
    @Resource
    private StudentService studentService;
    @Resource
    private HomeService homeService;
    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 用户端新建账户
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
        account.setUserId(userId);
        accountService.save(account); // 保存账户信息到 account 表
        Long newAccountNo = accountService.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", "C")).getAcctNo();
        if (type.equals(CHECKING_ACCOUNT)) {
            return checkingService.addCheckingAccount(newAccountNo);
        } else if (type.equals(SAVINGS_ACCOUNT)) {
            return savingsService.addSavingsAccount(newAccountNo);
        } else {

            String loanType = accountAddRequest.getLoanType();
            if (StrUtil.isBlank(loanType)) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "请选择贷款类型");
            }
            loanService.addLoanAccount(newAccountNo, loanType);
            if (loanType.equals(STUDENT_LOAN)) {
                return studentService.addStudentLoanAccount(newAccountNo);
            } else if (loanType.equals(HOME_LOAN)) {
                return homeService.addHomeAccount(newAccountNo);
            } else {
                return personalService.addPersonalAccount(newAccountNo);
            }
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
     * 用户端根据当前 loginUser 获取账户列表
     *
     * @param httpServletRequest
     * @return
     */
    @Operation(summary = "获取账户列表")
    @GetMapping("/list/vo")
    public BaseResponse<List<Object>> getAccountVOList(HttpServletRequest httpServletRequest) {
        Long userId = userService.getLoginUser(httpServletRequest).getId();
        List<Object> accountVOList = new ArrayList<>();
        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Account::getUserId, userId);
        List<Account> list = accountService.list(lambdaQueryWrapper);
        for (Account account : list) {
            String type = account.getType();
            if (type.equals(CHECKING_ACCOUNT)) {
                CheckingAccountVO checkingAccountVO = checkingService.getCheckingVO(userId);
                accountVOList.add(checkingAccountVO);
            } else if (type.equals(SAVINGS_ACCOUNT)) {
                SavingsAccountVO savingsAccountVO = savingsService.getSavingsVO(userId);
                accountVOList.add(savingsAccountVO);
            } else {
                Long acctNo = account.getAcctNo();
                String loanType = loanService.getById(acctNo).getLoanType();
                if (loanType.equals(PERSONAL_LOAN)) {
                    PersonalLoanVO personalLoanVO = personalService.getPersonalLoanVO(acctNo);
                    accountVOList.add(personalLoanVO);
                } else if (loanType.equals(STUDENT_LOAN)) {
                    StudentLoanVO studentLoanVO = studentService.getStudentLoanVO(acctNo);
                    accountVOList.add(studentLoanVO);
                } else {
                    HomeLoanVO homeLoanVO = homeService.getHomeLoanVO((acctNo));
                    accountVOList.add(homeLoanVO);
                }
            }
        }
        return ResultUtils.success(accountVOList);
    }
}
