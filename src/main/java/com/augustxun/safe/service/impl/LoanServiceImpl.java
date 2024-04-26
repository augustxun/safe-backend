package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.vo.LoanAccountVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.HomeService;
import com.augustxun.safe.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.service.LoanService;
import com.augustxun.safe.mapper.LoanMapper;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
* @author augustxun
* @description 针对表【loan】的数据库操作Service实现
* @createDate 2024-04-21 11:59:05
*/
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper, Loan>
    implements LoanService{
    @Resource
    private AccountService accountService;
    @Resource
    private StudentService studentService;
    @Resource
    private HomeService homeService;
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

    @Override
    public LoanAccountVO getLoanAccount(Long userId) {
        Account loanAccount = accountService.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", "L"));
        if (loanAccount != null) {
            Loan loan = this.getById(loanAccount.getAcctNo());
            LoanAccountVO loanAccountVO = new LoanAccountVO();
            BeanUtils.copyProperties(loanAccount, loanAccountVO);
            BeanUtils.copyProperties(loan, loanAccountVO);
            return loanAccountVO;
        } else return null;
    }
}




