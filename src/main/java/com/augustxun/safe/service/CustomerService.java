package com.augustxun.safe.service;

import com.augustxun.safe.model.dto.customer.CustomerQueryRequest;
import com.augustxun.safe.model.dto.user.UserQueryRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.model.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author augustxun
 * @description 针对表【customer】的数据库操作Service
 * @createDate 2024-04-18 21:42:59
 */
public interface CustomerService extends IService<Customer> {
    public void validCustomer(Customer customer, boolean add);
    public QueryWrapper<Customer> getQueryWrapper(CustomerQueryRequest customerQueryRequest);
}
