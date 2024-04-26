package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.mapper.SavingsMapper;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.service.SavingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【savings】的数据库操作Service实现
 * @createDate 2024-04-21 11:59:05
 */
@Service
public class SavingsServiceImpl extends ServiceImpl<SavingsMapper, Savings> implements SavingsService {
    @Override
    public BaseResponse<String> addSavingsAccount(Long newAccountNo) {
        Savings savings = new Savings();
        savings.setAcctNo(newAccountNo);
        savings.setInterestRate(new BigDecimal("0.05"));
        savings.setBalance(new BigDecimal("0"));
        this.save(savings); // 保存账户信息到 checking 表
        return ResultUtils.success("创建成功");
    }
}




