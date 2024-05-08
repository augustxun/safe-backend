package com.augustxun.safe.controller.user;


import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.mapper.CustomerMapper;
import com.augustxun.safe.model.dto.customer.CustomerAddRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user/customer")
@Api(tags = "C端-客户管理接口")
@Slf4j
public class CustomerController {
    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param customerAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "新建客户信息")
    @PostMapping("/add")
    public BaseResponse<String> addCustomer(@RequestBody CustomerAddRequest customerAddRequest, HttpServletRequest request) {
        return customerService.addCustomerByUser(customerAddRequest, request);
    }

    /**
     * 更新
     *
     * @param customerUpdateRequest
     * @return
     */
    @Operation(summary = "更新客户信息")
    @PostMapping("/update")
    public BaseResponse<String> updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        return customerService.updateCustomer(customerUpdateRequest);
    }
}
