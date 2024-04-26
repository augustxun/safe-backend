package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.service.*;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author augustxun
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2024-04-18 21:41:29
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    public void validAccount(Account account, boolean add) {
        if (account == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = account.getAcctName();
        String type = account.getType();
        // 创建时，账户名或类型参数不能为空
        if (add) {
            if (StringUtils.isAnyBlank(name) || StringUtils.isAnyBlank(type)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public QueryWrapper<Account> getQueryWrapper(AccountQueryRequest accountQueryRequest) {
        if (accountQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String acctName = accountQueryRequest.getAcctName();
        Long userId = Long.parseLong(accountQueryRequest.getUserId());
        String city = accountQueryRequest.getCity();
        String state = accountQueryRequest.getState();
        String type = accountQueryRequest.getType();
        String sortField = accountQueryRequest.getSortField();
        String sortOrder = accountQueryRequest.getSortOrder();
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();


        queryWrapper.like(StringUtils.isNotBlank(acctName), "acctName", acctName);
        queryWrapper.like(StringUtils.isNotBlank(city), "city", city);
        queryWrapper.like(StringUtils.isNotBlank(state), "state", state);
        queryWrapper.like(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.eq((userId != null) && (userId > 0), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }







}




