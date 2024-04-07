package com.augustxun.safe.service.impl;

import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author augustxun
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2024-04-05 16:46:48
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
        implements IService<Account> {

}




