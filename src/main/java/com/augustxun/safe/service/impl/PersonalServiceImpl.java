package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.dto.personal.PersonalQueryRequest;
import com.augustxun.safe.model.entity.*;
import com.augustxun.safe.model.vo.PersonalLoanVO;
import com.augustxun.safe.model.vo.PersonalLoanVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.LoanService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.service.PersonalService;
import com.augustxun.safe.mapper.PersonalMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
* @author augustxun
* @description 针对表【personal】的数据库操作Service实现
* @createDate 2024-04-26 12:17:08
*/
@Service
public class PersonalServiceImpl extends ServiceImpl<PersonalMapper, Personal>
    implements PersonalService{

    @Override
    public QueryWrapper<Personal> getQueryWrapper(PersonalQueryRequest personalQueryRequest) {
        if (personalQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String sortField =personalQueryRequest.getSortField();
        String sortOrder = personalQueryRequest.getSortOrder();
        QueryWrapper<Personal> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public BaseResponse<String> addPersonalAccount(Long newAccountNo) {
        Personal personal = new Personal();
        personal.setAcctNo(newAccountNo);
        personal.setIncome(new BigDecimal("0.0"));
        personal.setPurpose("I apply this loan for ...");
        personal.setCreditScore(new BigDecimal("0.0"));
        this.save(personal);
        return ResultUtils.success("创建成功");
    }
}




