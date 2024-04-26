package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.dto.checking.CheckingQueryRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.model.vo.CheckingAccountVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author augustxun
 * @description 针对表【CheckingQueryRequest(账户)】的数据库操作Service
 * @createDate 2024-04-21 11:59:05
 */
public interface CheckingService extends IService<Checking> {
    /**
     * 新建一个 Checking 账户
     * @param acctNo
     * @return
     */
    public BaseResponse<String> addCheckingAccount(Long acctNo);
    /**
     * Checking账户查询条件
     *
     * @param checkingQueryRequest
     * @return
     */
    public QueryWrapper<Checking> getQueryWrapper(CheckingQueryRequest checkingQueryRequest);
}
