package com.augustxun.safe.mapper;

import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.vo.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author augustxun
* @description 针对表【account】的数据库操作Mapper
* @createDate 2024-04-18 21:41:29
* @Entity com.augustxun.safe.model.entity.Account
*/
public interface AccountMapper extends BaseMapper<Account> {
    CheckingVO selectCheckingVOById(Long id);
    SavingsVO selectSavingsVOById(Long id);
    HomeLoanVO selectHomeLoanVOById(Long id);
    StudentLoanVO selectStudentLoanVOById(Long id);
    PersonalLoanVO selectPersonalLoanVOById(Long id);
    List<CheckingVO> listCheckingVO();
    List<SavingsVO> listSavingsVO();
    List<HomeLoanVO> listHomeLoanVO();
    List<StudentLoanVO> listStudentLoanVO();
    List<PersonalLoanVO> listPersonalLoanVO();

}




