package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.mapper.CheckingMapper;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.service.CheckingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【checking(账户)】的数据库操作Service实现
 * @createDate 2024-04-21 11:59:05
 */
@Service
public class CheckingServiceImpl extends ServiceImpl<CheckingMapper, Checking>
        implements CheckingService {
    @Override
    public BaseResponse<String> addCheckingAccount(Long newAccountNo) {
        Checking checking = new Checking();
        checking.setAcctNo(newAccountNo);
        checking.setServiceFee(new BigDecimal("300.00"));
        checking.setBalance(new BigDecimal("0"));
        //        checking.setCustomerId(userCustomerId);
        this.save(checking); // 保存账户信息到 checking 表
        return ResultUtils.success("创建成功");
    }
}




