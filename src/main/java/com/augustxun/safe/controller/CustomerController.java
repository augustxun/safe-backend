package com.augustxun.safe.controller;

import cn.hutool.db.Session;
import com.augustxun.safe.common.R;
import com.augustxun.safe.model.dto.LoginFormDTO;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Slf4j
public class CustomerController {

    @Resource
    CustomerService customerService;

    @GetMapping("/sendCode")
    public R<String> send(@RequestParam("phone") String phone, HttpSession session) {
        return customerService.sendCode(phone, session);
    }

    @GetMapping("/login")
    public R<String> login(@RequestBody LoginFormDTO loginFormDTO, HttpSession session) {
        return customerService.login(loginFormDTO, session);
    }


}
