package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.checking.CheckingQueryRequest;
import com.augustxun.safe.model.dto.savings.SavingsQueryRequest;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.model.vo.SavingsAccountVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author augustxun
* @description 针对表【savings】的数据库操作Service
* @createDate 2024-04-21 11:59:05
*/
public interface SavingsService extends IService<Savings> {
    /**
     * 新建一个 Savings 账户
     * @param acctNo
     * @return
     */
    public BaseResponse<String> addSavingsAccount(Long acctNo);

    /**
     * 根据用户 Id 获取Savings 账户
     * @param userId
     * @return
     */
    public SavingsAccountVO getSavingsVO(Long userId);
    /**
     * Savings账户查询条件
     *
     * @param savingsQueryRequest
     * @return
     */
    public QueryWrapper<Savings> getQueryWrapper(SavingsQueryRequest savingsQueryRequest);
}
