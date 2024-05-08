package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.account.AccountAddRequest;
import com.augustxun.safe.model.dto.account.AccountQueryRequest;
import com.augustxun.safe.model.dto.account.AccountUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author augustxun
 * @description 针对表【account】的数据库操作Service
 * @createDate 2024-04-18 21:41:29
 */
public interface AccountService extends IService<Account> {
    /**
     * 校验参数
     *
     * @param account
     * @param add
     */
    public void validAccount(Account account, boolean add);

    /**
     * 账户查询条件
     *
     * @param accountQueryRequest
     * @return
     */
    public QueryWrapper<Account> getQueryWrapper(AccountQueryRequest accountQueryRequest);

    /**
     * 新建账户，保存账户到各级子表
     * @param newAccountNo
     * @param type
     * @param accountAddRequest
     * @return
     */
    public BaseResponse<String> saveAccounts(Long newAccountNo, String type, AccountAddRequest accountAddRequest);

    /**
     * 删除各级子表账户
     * @param acctNo
     * @return
     */
    boolean deleteAccounts(Long acctNo);
    /**
     * 获取 Checking 账户视图
     * @param userId
     * @return
     */
    public CheckingVO getCheckingVO(Long userId);

    /**
     * 获取 Savings 账户视图
     * @param userId
     * @return
     */
    public SavingsVO getSavingsVO(Long userId);

    /**
     * 获取 HomeLoan 账户视图
     * @param userId
     * @return
     */
    public HomeLoanVO getHomeLoanVO(Long userId);

    /**
     * 获取 StudentLoan 账户视图
     * @param userId
     * @return
     */
    public StudentLoanVO getStudentLoanVO(Long userId);

    /**
     * 获取 PersonalLoan 账户视图
     * @param userId
     * @return
     */
    public PersonalLoanVO getPersonalLoanVO(Long userId);

    /**
     * 获取账户 VO 视图列表
     * @param list
     * @param list
     */
    List<Object> getAccountVOList(List<Account> list);

    /**
     *
     * @param userId
     * @param type
     * @return
     */
    Object getAccountVO(Long acctNo,Long userId, String type);


    Page<CheckingVO> listCheckingVOByPage(int current, int pageSize);

    Page<SavingsVO> listSavingsVOByPage(int current, int pageSize);

    Page<HomeLoanVO> listHomeLoanVOByPage(int current, int pageSize);

    Page<StudentLoanVO> listStudentLoanVOByPage(int current, int pageSize);

    Page<PersonalLoanVO> listPersonalLoanVOByPage(int current, int pageSize);

    BaseResponse<Boolean> updateAccount(AccountUpdateRequest accountUpdateRequest);

    /**
     * 检查用户是否存在
     * @param userId
     * @param type
     * @return
     */
    boolean queryIfExistsAccount(Long userId, String type);

    /**
     * 管理员新建账户接口
     * @param accountAddRequest
     * @return
     */
    BaseResponse<String> addAccountByAdmin(AccountAddRequest accountAddRequest, HttpServletRequest request);

    /**
     * 用户端新建账户接口
     * @param accountAddRequest
     * @param request
     * @return
     */
    BaseResponse<String> addAccountByUser(AccountAddRequest accountAddRequest, HttpServletRequest request);
}
