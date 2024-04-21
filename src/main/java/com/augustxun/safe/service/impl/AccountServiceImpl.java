package com.augustxun.safe.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.vo.AccountVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author augustxun
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2024-04-18 21:41:29
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
        implements AccountService {
    public void validAccount(Account account, boolean add) {
        if (account == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = account.getAcctName();
        // 创建时，参数不能为空
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
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
        Long userId = accountQueryRequest.getUserId();
        String city = accountQueryRequest.getCity();
        String state = accountQueryRequest.getState();
        String type = accountQueryRequest.getType();
        String sortField = accountQueryRequest.getSortField();
        String sortOrder = accountQueryRequest.getSortOrder();
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();


        queryWrapper.like(StringUtils.isNotBlank(acctName), "acctName", acctName);
        queryWrapper.like(StringUtils.isNotBlank(city), "city", city);
        queryWrapper.like(StringUtils.isNotBlank(state), "city", state);
        queryWrapper.like(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.eq((userId != null) && (userId > 0), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public Page<AccountVO> getAccountVOPage(Page<Account> accountPage) {
        List<Account> accountList = accountPage.getRecords();
        Page<AccountVO> accountVOPage = new Page<>(accountPage.getCurrent(), accountPage.getSize(), accountPage.getTotal());
        if (CollUtil.isEmpty(accountList)) {
            return accountVOPage;
        }
        // 填充信息
        List<AccountVO> accountVOList = accountList.stream().map(account -> {
            AccountVO accountVO = AccountVO.objToVo(account);
            return accountVO;
        }).collect(Collectors.toList());
        accountVOPage.setRecords(accountVOList);
        return accountVOPage;
    }
}




