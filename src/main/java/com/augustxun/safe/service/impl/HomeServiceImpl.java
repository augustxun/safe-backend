package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.HomeMapper;
import com.augustxun.safe.model.dto.home.HomeQueryRequest;
import com.augustxun.safe.model.dto.home.HomeUpdateRequest;
import com.augustxun.safe.model.entity.*;
import com.augustxun.safe.model.vo.HomeLoanVO;
import com.augustxun.safe.model.vo.PersonalLoanVO;
import com.augustxun.safe.model.vo.StudentLoanVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.HomeService;
import com.augustxun.safe.service.LoanService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【home】的数据库操作Service实现
 * @createDate 2024-04-26 00:04:25
 */
@Service
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Home> implements HomeService {
    @Override
    public BaseResponse<String> addHomeAccount(Long acctNo) {
        Home home = new Home();
        home.setAcctNo(acctNo);
        home.setBuiltYear(1900);
        home.setYearlyPremium(new BigDecimal("0.0"));
        home.setInsureAcctNo(0L);
        home.setInsureComId(1L);
        this.save(home);
        return ResultUtils.success("创建成功");
    }
    @Override
    public QueryWrapper<Home> getQueryWrapper(HomeQueryRequest homeQueryRequest) {
        if (homeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String sortField =homeQueryRequest.getSortField();
        String sortOrder = homeQueryRequest.getSortOrder();
        QueryWrapper<Home> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public BaseResponse<Boolean> updateHome(HomeUpdateRequest homeUpdateRequest) {
        long acctNo = Long.parseLong(homeUpdateRequest.getAcctNo());

        if (homeUpdateRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Home home = new Home();
        BeanUtils.copyProperties(homeUpdateRequest, home);
        home.setAcctNo(acctNo);
        // 判断是否存在
        Home oldHome = this.getById(acctNo);
        ThrowUtils.throwIf(oldHome == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = this.updateById(home);
        return ResultUtils.success(result);
    }
}




