package com.augustxun.safe.service.impl;

import com.augustxun.safe.mapper.PersonalMapper;
import com.augustxun.safe.model.entity.Personal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author augustxun
 * @description 针对表【personal】的数据库操作Service实现
 * @createDate 2024-04-05 17:23:03
 */
@Service
public class PersonalServiceImpl extends ServiceImpl<PersonalMapper, Personal>
        implements IService<Personal> {

}




