package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.mapper.CheckingMapper;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.model.vo.CheckingAccountVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.CheckingService;
import com.augustxun.safe.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【checking(账户)】的数据库操作Service实现
 * @createDate 2024-04-21 11:59:05
 */
@Service
public class CheckingServiceImpl extends ServiceImpl<CheckingMapper, Checking>
        implements CheckingService {
    @Resource
    private AccountService accountService;
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

    @Override
    public CheckingAccountVO getCheckingVO(Long userId) {
        Account checkingAccount = accountService.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", "C"));
        if (checkingAccount != null) {
            Checking checking = this.getById(checkingAccount.getAcctNo());
            CheckingAccountVO checkingAccountVO = new CheckingAccountVO();
            BeanUtils.copyProperties(checking, checkingAccountVO);
            BeanUtils.copyProperties(checkingAccount, checkingAccountVO);
            return checkingAccountVO;
        } else return null;
    }
}




