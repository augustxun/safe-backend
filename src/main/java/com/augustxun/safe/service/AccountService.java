package com.augustxun.safe.service;

import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.vo.AccountVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author augustxun
 * @description 针对表【account】的数据库操作Service
 * @createDate 2024-04-18 21:41:29
 */
public interface AccountService extends IService<Account> {
    public void validAccount(Account account, boolean add);

    public QueryWrapper<Account> getQueryWrapper(AccountQueryRequest accountQueryRequest);

    public Page<AccountVO> getAccountVOPage(Page<Account> accountPage);
}
