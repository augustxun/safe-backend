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
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.model.vo.*;
import com.augustxun.safe.service.*;
import com.augustxun.safe.utils.PageUtils;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @Resource
    private AccountMapper accountMapper;

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
        if (type.equals(CHECKING_ACCOUNT)) {
            b = checkingService.removeById(acctNo);
        } else if (type.equals(SAVINGS_ACCOUNT)) {
            b = savingsService.removeById(acctNo);
        } else {
            Loan loan = loanService.getById(acctNo);
            String loanType = loan.getLoanType();
            if (loanType.equals(STUDENT_LOAN)) {
                b = studentService.removeById(acctNo);
            } else if (loanType.equals(HOME_LOAN)) {
                b = homeService.removeById(acctNo);
            } else b = personalService.removeById(acctNo);
            b = loanService.removeById(acctNo);
        }
        b = this.removeById(acctNo);
        return b;
    }

    @Override
    public CheckingVO getCheckingVO(Long userId) {
        return accountMapper.selectCheckingVOById(userId);
    }

    @Override
    public SavingsVO getSavingsVO(Long userId) {
        return accountMapper.selectSavingsVOById(userId);
    }

    @Override
    public StudentLoanVO getStudentLoanVO(Long userId) {
        return accountMapper.selectStudentLoanVOById(userId);
    }

    @Override
    public HomeLoanVO getHomeLoanVO(Long userId) {
        return accountMapper.selectHomeLoanVOById(userId);
    }

    @Override
    public PersonalLoanVO getPersonalLoanVO(Long userId) {
        return accountMapper.selectPersonalLoanVOById(userId);
    }

    @Override
    public List<Object> getAccountVOList(List<Account> list) {
        List<Object> accountVOList = new ArrayList<>();
        for (Account account : list) {
            String type = account.getType();
            Long userId = account.getUserId();
            if (type.equals(CHECKING_ACCOUNT)) {
                CheckingVO CheckingVO = getCheckingVO(userId);
                accountVOList.add(CheckingVO);
            } else if (type.equals(SAVINGS_ACCOUNT)) {
                SavingsVO SavingsVO = getSavingsVO(userId);
                accountVOList.add(SavingsVO);
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

    @Override
    public Object getAccountVO(Long acctNo, Long userId, String type) {
        if (type.equals(CHECKING_ACCOUNT)) {
            return getCheckingVO(userId);
        } else if (type.equals(SAVINGS_ACCOUNT)) {
            return getSavingsVO(userId);
        } else {
            String loanType = loanService.getById(acctNo).getLoanType();
            if (loanType.equals(PERSONAL_LOAN)) {
                return getPersonalLoanVO(userId);
            } else if (loanType.equals(STUDENT_LOAN)) {
                return getStudentLoanVO(userId);
            } else {
                return getHomeLoanVO(userId);
            }
        }
    }

    @Override
    public Page<CheckingVO> listCheckingVOByPage(int current, int pageSize) {
        List<CheckingVO> checkingVOList = accountMapper.listCheckingVO();
        return PageUtils.getPages(current, pageSize, checkingVOList);
    }

    @Override
    public Page<SavingsVO> listSavingsVOByPage(int current, int pageSize) {
        List<SavingsVO> savingsVOList = accountMapper.listSavingsVO();
        return PageUtils.getPages(current, pageSize, savingsVOList);
    }

    @Override
    public Page<HomeLoanVO> listHomeLoanVOByPage(int current, int pageSize) {
        List<HomeLoanVO> homeLoanVOList = accountMapper.listHomeLoanVO();
        return PageUtils.getPages(current, pageSize, homeLoanVOList);
    }

    @Override
    public Page<StudentLoanVO> listStudentLoanVOByPage(int current, int pageSize) {
        List<StudentLoanVO> studentLoanVOList = accountMapper.listStudentLoanVO();
        return PageUtils.getPages(current, pageSize, studentLoanVOList);
    }

    @Override
    public Page<PersonalLoanVO> listPersonalLoanVOByPage(int current, int pageSize) {
        List<PersonalLoanVO> personalLoanVOList = accountMapper.listPersonalLoanVO();
        return PageUtils.getPages(current, pageSize, personalLoanVOList);
    }


}




