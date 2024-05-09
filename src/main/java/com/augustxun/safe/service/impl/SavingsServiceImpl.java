package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.SavingsMapper;
import com.augustxun.safe.model.dto.savings.SavingsQueryRequest;
import com.augustxun.safe.model.dto.savings.SavingsUpdateRequest;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.service.SavingsService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【savings】的数据库操作Service实现
 * @createDate 2024-04-21 11:59:05
 */
@Service
public class SavingsServiceImpl extends ServiceImpl<SavingsMapper, Savings> implements SavingsService {
    @Transactional
    @Override
    public BaseResponse<String> addSavingsAccount(Long newAccountNo) {
        Savings savings = new Savings();
        savings.setAcctNo(newAccountNo);
        savings.setInterestRate(new BigDecimal("0.05"));
        savings.setBalance(new BigDecimal("0"));
        this.save(savings); // 保存账户信息到 CheckingQueryRequest 表
        return ResultUtils.success("创建成功");
    }
    @Override
    public QueryWrapper<Savings> getQueryWrapper(SavingsQueryRequest savingsQueryRequest) {
        if (savingsQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String sortField = savingsQueryRequest.getSortField();
        String sortOrder = savingsQueryRequest.getSortOrder();
        QueryWrapper<Savings> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Transactional
    @Override
    public BaseResponse<Boolean> updateSavings(SavingsUpdateRequest savingsUpdateRequest) {
        long acctNo = Long.parseLong(savingsUpdateRequest.getAcctNo());

        if (savingsUpdateRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Savings savings = new Savings();
        BeanUtils.copyProperties(savingsUpdateRequest, savings);
        savings.setAcctNo(acctNo);
        // 判断是否存在
        Savings oldSavings = this.getById(acctNo);
        ThrowUtils.throwIf(oldSavings == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = this.updateById(savings);
        return ResultUtils.success(result);
    }
}




