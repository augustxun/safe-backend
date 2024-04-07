package com.augustxun.safe.service.impl;

import com.augustxun.safe.mapper.SavingsMapper;
import com.augustxun.safe.model.entity.Savings;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author augustxun
 * @description 针对表【savings】的数据库操作Service实现
 * @createDate 2024-04-05 17:23:06
 */
@Service
public class SavingsServiceImpl extends ServiceImpl<SavingsMapper, Savings>
        implements IService<Savings> {

}




