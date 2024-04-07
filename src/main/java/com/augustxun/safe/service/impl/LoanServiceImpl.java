package com.augustxun.safe.service.impl;

import com.augustxun.safe.mapper.LoanMapper;
import com.augustxun.safe.model.entity.Loan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author augustxun
 * @description 针对表【loan】的数据库操作Service实现
 * @createDate 2024-04-05 17:22:59
 */
@Service
public class LoanServiceImpl extends ServiceImpl<LoanMapper, Loan>
        implements IService<Loan> {

}




