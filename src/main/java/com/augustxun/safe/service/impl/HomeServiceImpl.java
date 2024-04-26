package com.augustxun.safe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.model.entity.Home;
import com.augustxun.safe.service.HomeService;
import com.augustxun.safe.mapper.HomeMapper;
import org.springframework.stereotype.Service;

/**
* @author augustxun
* @description 针对表【home】的数据库操作Service实现
* @createDate 2024-04-26 00:04:25
*/
@Service
public class HomeServiceImpl extends ServiceImpl<HomeMapper, Home>
    implements HomeService{

}




