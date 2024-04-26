package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.mapper.SavingsMapper;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.model.vo.SavingsAccountVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.SavingsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【savings】的数据库操作Service实现
 * @createDate 2024-04-21 11:59:05
 */
@Service
public class SavingsServiceImpl extends ServiceImpl<SavingsMapper, Savings> implements SavingsService {
    @Resource
    private AccountService accountService;
    @Override
    public BaseResponse<String> addSavingsAccount(Long newAccountNo) {
        Savings savings = new Savings();
        savings.setAcctNo(newAccountNo);
        savings.setInterestRate(new BigDecimal("0.05"));
        savings.setBalance(new BigDecimal("0"));
        this.save(savings); // 保存账户信息到 CheckingQueryRequest 表
        return ResultUtils.success("创建成功");
    }

    @Override
    public SavingsAccountVO getSavingsVO(Long userId) {
        Account savingsAccount = accountService.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", "S"));
        if (savingsAccount != null) {
            Savings savings = this.getById(savingsAccount.getAcctNo());
            SavingsAccountVO savingsAccountVO = new SavingsAccountVO();
            BeanUtils.copyProperties(savingsAccount, savingsAccountVO);
            BeanUtils.copyProperties(savings, savingsAccountVO);
            return savingsAccountVO;
        } else return null;
    }
}




