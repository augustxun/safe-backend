package com.augustxun.safe.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.model.vo.AccountVO;
import com.augustxun.safe.service.*;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author augustxun
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2024-04-18 21:41:29
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    private CheckingService checkingService;

    @Resource
    private SavingsService savingsService;

    @Resource
    private LoanService loanService;

    @Resource
    private AccountService accountService;
    @Resource
    private UserService userService;

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
        queryWrapper.like(StringUtils.isNotBlank(state), "city", state);
        queryWrapper.like(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.eq((userId != null) && (userId > 0), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }







}




