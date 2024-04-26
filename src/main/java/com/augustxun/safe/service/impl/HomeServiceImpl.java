package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.mapper.HomeMapper;
import com.augustxun.safe.model.dto.home.HomeQueryRequest;
import com.augustxun.safe.model.entity.Home;
import com.augustxun.safe.service.HomeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
        home.setInsureComId(0L);
        this.save(home);
        return ResultUtils.success("创建成功");
    }

    @Override
    public QueryWrapper<Home> getQueryWrapper(HomeQueryRequest homeQueryRequest) {
        return null;
    }
}




