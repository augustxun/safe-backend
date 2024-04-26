package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.service.LoanService;
import com.augustxun.safe.mapper.LoanMapper;
import org.apache.poi.hpsf.Decimal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
* @author augustxun
* @description 针对表【loan】的数据库操作Service实现
* @createDate 2024-04-21 11:59:05
*/
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper, Loan>
    implements LoanService{
    @Override
    public BaseResponse<String> addLoanAccount(Long newAccountNo){
        Loan loan = new Loan();
        loan.setAcctNo(newAccountNo);
        loan.setAmount(new BigDecimal("0.0"));
        loan.setMonths(12);
        loan.setRate(new BigDecimal("0.05"));
        loan.setMonths(20);
        this.save(loan); // 保存账户信息到 checking 表
        return ResultUtils.success("创建成功");
    }
}




