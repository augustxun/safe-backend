package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.vo.LoanAccountVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author augustxun
* @description 针对表【loan】的数据库操作Service
* @createDate 2024-04-21 11:59:05
*/
public interface LoanService extends IService<Loan> {
    /**
     * 新建一个 Loan 账户
     * @param acctNo
     * @return
     */
    public BaseResponse<String> addLoanAccount(Long acctNo);
    public LoanAccountVO getLoanAccount(Long userId);
}
