package com.augustxun.safe.service;

import com.augustxun.safe.model.dto.loan.LoanQueryRequest;
import com.augustxun.safe.model.dto.personal.PersonalQueryRequest;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.entity.Personal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author augustxun
 * @description 针对表【personal】的数据库操作Service
 * @createDate 2024-04-26 12:17:08
 */
public interface PersonalService extends IService<Personal> {
    /**
     * Personal账户查询条件
     *
     * @param personalQueryRequest
     * @return
     */
    public QueryWrapper<Personal> getQueryWrapper(PersonalQueryRequest personalQueryRequest);
}
