package com.augustxun.safe.service.impl;

import com.augustxun.safe.model.dto.personal.PersonalQueryRequest;
import com.augustxun.safe.model.entity.Personal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.service.PersonalService;
import com.augustxun.safe.mapper.PersonalMapper;
import org.springframework.stereotype.Service;

/**
* @author augustxun
* @description 针对表【personal】的数据库操作Service实现
* @createDate 2024-04-26 12:17:08
*/
@Service
public class PersonalServiceImpl extends ServiceImpl<PersonalMapper, Personal>
    implements PersonalService{

    @Override
    public QueryWrapper<Personal> getQueryWrapper(PersonalQueryRequest personalQueryRequest) {
        return null;
    }
}




