package com.augustxun.safe.service.impl;

import cn.hutool.core.util.StrUtil;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.AccountMapper;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.*;
import com.augustxun.safe.service.*;
import com.augustxun.safe.utils.PageUtils;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private UserService userService;

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
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    @Transactional
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
    @Transactional
    public boolean deleteAccounts(Long acctNo) {
        if (acctNo == null || acctNo <= 0) {
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
            } else {b = personalService.removeById(acctNo);}
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

    @Override
    @Transactional
    public BaseResponse<Boolean> updateAccount(AccountUpdateRequest accountUpdateRequest) {
        long acctNo = Long.parseLong(accountUpdateRequest.getAcctNo());
        // 1.判断旧的 account 信息是否存在
        Account oldAccount = this.getById(acctNo);
        ThrowUtils.throwIf(oldAccount == null, ErrorCode.NOT_FOUND_ERROR);
        // 2.根据 id 更新account 表
        Account account = new Account();
        BeanUtils.copyProperties(accountUpdateRequest, account);
        account.setAcctNo(acctNo);
        boolean result = this.updateById(account);

        return ResultUtils.success(result);
    }

    public boolean queryIfExistsAccount(Long userId, String type) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserId, userId).eq(Account::getType, type);
        Account accountServiceOne = this.getOne(queryWrapper);
        // 已被创建，操作失败
        if (accountServiceOne != null) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public BaseResponse<String> addAccountByAdmin(AccountAddRequest accountAddRequest, HttpServletRequest request) {
        // 1.检查当前用户是否已登录
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登陆");
        }
        // 2.检查请求体
        if (accountAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.创建账户
        String type = accountAddRequest.getType(); // 账户类型
        // 2.1 检查该类型账户是否已经被创建
        String uId = accountAddRequest.getUserId();
        if (uId == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "管理员操作，请指定账户所属用户");
        }
        // 3.查询当前用户是否存在
        Long userId = Long.parseLong(uId);
        User user = userService.getById(userId);
        if (user == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 4.检查账号是否存在
        boolean b = this.queryIfExistsAccount(userId, type);
        if (b) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "该用户已有" + type + "类账户，请勿重复创建");
        }
        // 5.保存账户信息到 account 表
        Account account = new Account();
        BeanUtils.copyProperties(accountAddRequest, account);
        account.setUserId(userId);
        this.save(account);
        // 6.插入数据到子表
        Long newAccountNo = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", type)).getAcctNo();
        return this.saveAccounts(newAccountNo, type, accountAddRequest);
    }

    @Override
    @Transactional
    public BaseResponse<String> addAccountByUser(AccountAddRequest accountAddRequest, HttpServletRequest request) {
        // 1.检查当前用户是否已登录
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先登陆");
        }
        // 2.检查当前用户是否具备客户信息
        Long customerId = loginUser.getCustomerId();
        if (customerId == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "请先填写个人信息");
        }
        // 3.检查请求体
        if (accountAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2.添加账户
        String type = accountAddRequest.getType(); // 账户类型
        Long userId = loginUser.getId(); // 账户 userId
        // 3.检查该类型账户是否已经被创建
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserId, userId).eq(Account::getType, type);
        Account accountServiceOne = this.getOne(queryWrapper);
        // 3.1 已被创建，返回失败
        if (accountServiceOne != null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "该用户已有" + type + "类账户，请勿重复创建");
        }
        // 3.2 未被创建，创建账户
        Account account = new Account();
        BeanUtils.copyProperties(accountAddRequest, account);
        account.setUserId(userId);
        // 4.保存账户信息到 account 表
        this.save(account);
        // 5.在子表中插入一条数据
        Long newAccountNo = this.getOne(new QueryWrapper<Account>().eq("userId", userId).eq("type", type)).getAcctNo();
        return this.saveAccounts(newAccountNo, type, accountAddRequest);
    }



}




