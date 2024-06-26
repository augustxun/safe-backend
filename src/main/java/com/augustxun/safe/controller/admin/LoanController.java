package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.model.dto.loan.LoanQueryRequest;
import com.augustxun.safe.model.dto.loan.LoanUpdateRequest;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.service.LoanService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-Loan账号管理接口")
@RequestMapping("admin/loan")
@Slf4j
public class LoanController {
    @Resource
    private LoanService loanService;

    /**
     * Loan账户信息分页查询
     *
     * @param loanQueryRequest
     * @return
     */
    @Operation(summary = "Loan账户信息分页查询")
    @ApiIgnore
    @PostMapping("/list/page")
    
    public BaseResponse<Page<Loan>> listLoanByPage(@RequestBody LoanQueryRequest loanQueryRequest) {
        int current = loanQueryRequest.getCurrent();
        int size = loanQueryRequest.getPageSize();
        Page<Loan> loanPage = loanService.page(new Page<>(current, size), loanService.getQueryWrapper(loanQueryRequest));
        return ResultUtils.success(loanPage);
    }

    /**
     * 更新 LOAN 账户
     *
     * @param loanUpdateRequest
     * @return
     */
    @Operation(summary = "更新Loan表账户数据")
    @PostMapping("/update")
    
    public BaseResponse<Boolean> updateLoan(@RequestBody LoanUpdateRequest loanUpdateRequest) {
        return loanService.updateLoan(loanUpdateRequest);
    }
}
