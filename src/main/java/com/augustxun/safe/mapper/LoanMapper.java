package com.augustxun.safe.mapper;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.entity.Loan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author augustxun
* @description 针对表【loan】的数据库操作Mapper
* @createDate 2024-04-21 11:59:05
* @Entity generator.domain.Loanentity
*/
public interface LoanMapper extends BaseMapper<Loan> {

    List<Object> queryPersonalData();

    List<Object> queryHomeData();
}




