package com.augustxun.safe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.model.entity.Loan;
import com.augustxun.safe.service.LoanService;
import com.augustxun.safe.mapper.LoanMapper;
import org.springframework.stereotype.Service;

/**
* @author augustxun
* @description 针对表【loan】的数据库操作Service实现
* @createDate 2024-04-21 11:59:05
*/
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper, Loan>
    implements LoanService{

}




