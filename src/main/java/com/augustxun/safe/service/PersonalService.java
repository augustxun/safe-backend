package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.loan.LoanQueryRequest;
import com.augustxun.safe.model.dto.personal.PersonalQueryRequest;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.entity.Personal;
import com.augustxun.safe.model.vo.PersonalLoanVO;
import com.augustxun.safe.model.vo.StudentLoanVO;
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
    /**
     * 根据 userId 获取 PersonalLoan 账户视图
     * @param userId
     * @return
     */
    public PersonalLoanVO getPersonalLoanVO(Long userId);

    BaseResponse<String> addPersonalAccount(Long newAccountNo);
}
