package com.augustxun.safe.service;

import com.augustxun.safe.model.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author augustxun
* @description 针对表【account】的数据库操作Service
* @createDate 2024-04-18 21:41:29
*/
public interface AccountService extends IService<Account> {
    public void validAccount(Account account, boolean add);
}
