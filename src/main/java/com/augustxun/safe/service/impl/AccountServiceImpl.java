package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.entity.Account;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.mapper.AccountMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author augustxun
* @description 针对表【account】的数据库操作Service实现
* @createDate 2024-04-18 21:41:29
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{
    public void validAccount(Account account, boolean add) {
        if (account == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = account.getAcctName();
        // 创建时，参数不能为空
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }
}




