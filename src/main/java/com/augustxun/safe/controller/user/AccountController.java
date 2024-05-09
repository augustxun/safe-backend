package com.augustxun.safe.controller.user;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("user/account")
@Api(tags = "C端-账户管理接口")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;

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
    public BaseResponse<String> addAccount(@RequestBody AccountAddRequest accountAddRequest, HttpServletRequest request) {
        return accountService.addAccountByUser(accountAddRequest, request);
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


    /**
     * 用户端根据当前 loginUser 获取账户列表
     *
     * @param Id
     * @return
     */
    @Operation(summary = "获取账户列表")
    @GetMapping("/list/vo")
    public BaseResponse<List<Object>> getAccountVOList(@RequestParam String Id) {
        Long userId = Long.parseLong(Id);
        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, userId);
        List<Account> list = accountService.list(lambdaQueryWrapper);
        List<Object> accountVOList = accountService.getAccountVOList(list);
        return ResultUtils.success(accountVOList);
    }

    /**
     * 用户端根据 acctNo 获取账户数据视图
     *
     * @param acctId
     * @return
     */
    @Operation(summary = "根据 acctNo 获取账号VO")
    @GetMapping("get/vo")
    public BaseResponse<Object> getAccountVO(String acctId) {

        long acctNo = Long.parseLong(acctId);
        Account account = accountService.getById(acctNo);
        if (account == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前账户不存在");
        }
        String type = account.getType();
        Long userId = account.getUserId();
        Object accountVO = accountService.getAccountVO(acctNo, userId, type);
        if (accountVO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询失败");
        }
        return ResultUtils.success(accountVO);
    }
}
