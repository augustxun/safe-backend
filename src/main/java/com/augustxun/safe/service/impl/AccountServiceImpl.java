package com.augustxun.safe.service.impl;

import cn.hutool.core.util.StrUtil;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.entity.*;
import com.augustxun.safe.model.vo.*;
import com.augustxun.safe.service.*;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.augustxun.safe.constant.AccountConstants.*;

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
    private PersonalService personalService;
    @Resource
    private StudentService studentService;
    @Resource
    private HomeService homeService;

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
        queryWrapper.like(StringUtils.isNotBlank(state), "state", state);
        queryWrapper.like(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.eq((userId != null) && (userId > 0), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public BaseResponse<String> saveAccounts(Long newAccountNo, String type, AccountAddRequest accountAddRequest) {
        if (type.equals(CHECKING_ACCOUNT)) {
            return checkingService.addCheckingAccount(newAccountNo);
        } else if (type.equals(SAVINGS_ACCOUNT)) {
            return savingsService.addSavingsAccount(newAccountNo);
        } else {
            String loanType = accountAddRequest.getLoanType();
            if (StrUtil.isBlank(loanType)) {
                return ResultUtils.error(ErrorCode.OPERATION_ERROR, "请选择贷款类型");
            }
            loanService.addLoanAccount(newAccountNo, loanType);
            if (loanType.equals(STUDENT_LOAN)) {
                return studentService.addStudentLoanAccount(newAccountNo);
            } else if (loanType.equals(HOME_LOAN)) {
                return homeService.addHomeAccount(newAccountNo);
            } else {
                return personalService.addPersonalAccount(newAccountNo);
            }
        }
    }

    @Override
    public boolean deleteAccounts(DeleteRequest deleteRequest) {
        long acctNo = Long.parseLong(deleteRequest.getId());
        if (deleteRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Account oldAccount = this.getById(acctNo);
        String type = oldAccount.getType();
        boolean b;
        b = this.removeById(acctNo);
        if (type.equals(CHECKING_ACCOUNT)) {
            b = checkingService.removeById(acctNo);
        } else if (type.equals(SAVINGS_ACCOUNT)) {
            b = savingsService.removeById(acctNo);
        } else {
            Loan loan = loanService.getById(acctNo);
            String loanType = loan.getLoanType();
            b = loanService.removeById(acctNo);
            if (loanType.equals(STUDENT_LOAN)) {
                b = studentService.removeById(acctNo);
            } else if (loanType.equals(HOME_LOAN)) {
                b = homeService.removeById(acctNo);
            } else b = personalService.removeById(acctNo);
        }
        return b;
    }

    @Override
    public CheckingAccountVO getCheckingVO(Long userId) {
        Account checkingAccount = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", CHECKING_ACCOUNT));
        if (checkingAccount != null) {
            Checking checking = checkingService.getById(checkingAccount.getAcctNo());
            CheckingAccountVO checkingAccountVO = new CheckingAccountVO();
            BeanUtils.copyProperties(checking, checkingAccountVO);
            BeanUtils.copyProperties(checkingAccount, checkingAccountVO);
            return checkingAccountVO;
        } else return null;
    }

    @Override
    public SavingsAccountVO getSavingsVO(Long userId) {
        Account savingsAccount = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", SAVINGS_ACCOUNT));
        if (savingsAccount != null) {
            Savings savings = savingsService.getById(savingsAccount.getAcctNo());
            SavingsAccountVO savingsAccountVO = new SavingsAccountVO();
            BeanUtils.copyProperties(savingsAccount, savingsAccountVO);
            BeanUtils.copyProperties(savings, savingsAccountVO);
            return savingsAccountVO;
        } else return null;
    }
    @Override
    public StudentLoanVO getStudentLoanVO(Long userId) {
        Account account = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", LOAN_ACCOUNT));
        if (account != null) {
            Long acctNo = account.getAcctNo();
            Loan loan = loanService.getById(acctNo);
            Student student = studentService.getById(acctNo);
            StudentLoanVO studentLoanVO = new StudentLoanVO();
            BeanUtils.copyProperties(account, studentLoanVO);
            BeanUtils.copyProperties(loan, studentLoanVO);
            BeanUtils.copyProperties(student, studentLoanVO);
            return studentLoanVO;
        } else return null;
    }
    @Override
    public HomeLoanVO getHomeLoanVO(Long userId) {
        Account account = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", LOAN_ACCOUNT));
        if (account != null) {
            Long acctNo = account.getAcctNo();
            Loan loan = loanService.getById(acctNo);
            Home home = homeService.getById(acctNo);
            HomeLoanVO homeLoanVO = new HomeLoanVO();
            BeanUtils.copyProperties(account, homeLoanVO);
            BeanUtils.copyProperties(loan, homeLoanVO);
            BeanUtils.copyProperties(home, homeLoanVO);
            return homeLoanVO;
        } else return null;
    }
    @Override
    public PersonalLoanVO getPersonalLoanVO(Long userId) {
        Account account = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", LOAN_ACCOUNT));
        if (account != null) {
            Long acctNo = account.getAcctNo();
            Loan loan = loanService.getById(acctNo);
            Personal personal = personalService.getById(acctNo);
            PersonalLoanVO personalLoanVO = new PersonalLoanVO();
            BeanUtils.copyProperties(account, personalLoanVO);
            BeanUtils.copyProperties(loan, personalLoanVO);
            BeanUtils.copyProperties(personal, personalLoanVO);
            return personalLoanVO;
        } else return null;
    }
    @Override
    public List<Object> getAccountVOList(List<Account> list) {
        List<Object> accountVOList = new ArrayList<>();
        for (Account account : list) {
            String type = account.getType();
            Long userId = account.getUserId();
            if (type.equals(CHECKING_ACCOUNT)) {
                CheckingAccountVO checkingAccountVO = getCheckingVO(userId);
                accountVOList.add(checkingAccountVO);
            } else if (type.equals(SAVINGS_ACCOUNT)) {
                SavingsAccountVO savingsAccountVO = getSavingsVO(userId);
                accountVOList.add(savingsAccountVO);
            } else {
                Long acctNo = account.getAcctNo();
                String loanType = loanService.getById(acctNo).getLoanType();
                if (loanType.equals(PERSONAL_LOAN)) {
                    PersonalLoanVO personalLoanVO = getPersonalLoanVO(userId);
                    accountVOList.add(personalLoanVO);
                } else if (loanType.equals(STUDENT_LOAN)) {
                    StudentLoanVO studentLoanVO = getStudentLoanVO(userId);
                    accountVOList.add(studentLoanVO);
                } else {
                    HomeLoanVO homeLoanVO = getHomeLoanVO(userId);
                    accountVOList.add(homeLoanVO);
                }
            }
        }
        return accountVOList;
    }
}




