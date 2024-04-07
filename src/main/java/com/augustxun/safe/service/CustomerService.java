package com.augustxun.safe.service;

import com.augustxun.safe.common.R;
import com.augustxun.safe.model.dto.LoginFormDTO;
import com.augustxun.safe.model.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
 * @author augustxun
 * @description 针对表【customer】的数据库操作Service
 * @createDate 2024-04-05 17:14:01
 */
public interface CustomerService extends IService<Customer> {
    R login(LoginFormDTO loginFormDTO, HttpSession session);

    R<String> sendCode(String phone, HttpSession session);
}
