package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.customer.CustomerAddRequest;
import com.augustxun.safe.model.dto.customer.CustomerQueryRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.model.entity.Customer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author augustxun
 * @description 针对表【customer】的数据库操作Service
 * @createDate 2024-04-18 21:42:59
 */
public interface CustomerService extends IService<Customer> {
    public void validCustomer(Customer customer, boolean add);
    public QueryWrapper<Customer> getQueryWrapper(CustomerQueryRequest customerQueryRequest);

    BaseResponse<String> updateCustomer(CustomerUpdateRequest customerUpdateRequest);

    /**
     * 管理员创建客户信息
     * @param customerAddRequest
     * @return
     */
    BaseResponse<String> addCustomerByAdmin(CustomerAddRequest customerAddRequest);

    /**
     * 普通用户自己新建客户信息
     * @param customerAddRequest
     * @param request
     * @return
     */
    BaseResponse<String> addCustomerByUser(CustomerAddRequest customerAddRequest, HttpServletRequest request);
}
