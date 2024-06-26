package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.LoanMapper;
import com.augustxun.safe.model.dto.loan.LoanQueryRequest;
import com.augustxun.safe.model.dto.loan.LoanUpdateRequest;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.service.LoanService;
import com.augustxun.safe.service.PersonalService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
* @author augustxun
* @description 针对表【loan】的数据库操作Service实现
* @createDate 2024-04-21 11:59:05
*/
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper, Loan>
    implements LoanService{
    @Transactional
    @Override
    public BaseResponse<String> addLoanAccount(Long newAccountNo, String loanType){
        Loan loan = new Loan();
        loan.setAcctNo(newAccountNo);
        loan.setAmount(new BigDecimal("0.0"));
        loan.setPayment(new BigDecimal("30"));
        loan.setMonths(12);
        loan.setRate(new BigDecimal("0.05"));
        loan.setLoanType(loanType);
        this.save(loan); // 保存账户信息到 CheckingQueryRequest 表
        return ResultUtils.success("创建成功");
    }
    @Override
    public QueryWrapper<Loan> getQueryWrapper(LoanQueryRequest loanQueryRequest) {
        if (loanQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String sortField = loanQueryRequest.getSortField();
        String sortOrder = loanQueryRequest.getSortOrder();
        QueryWrapper<Loan> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Transactional
    @Override
    public BaseResponse<Boolean> updateLoan(LoanUpdateRequest loanUpdateRequest) {
        long acctNo = Long.parseLong(loanUpdateRequest.getAcctNo());

        if (loanUpdateRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Loan loan = new Loan();
        BeanUtils.copyProperties(loanUpdateRequest, loan);
        loan.setAcctNo(acctNo);
        // 判断是否存在
        Loan oldLoan = this.getById(acctNo);
        ThrowUtils.throwIf(oldLoan == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = this.updateById(loan);
        return ResultUtils.success(result);
    }

}




