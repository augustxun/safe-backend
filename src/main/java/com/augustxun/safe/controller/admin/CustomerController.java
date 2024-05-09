package com.augustxun.safe.controller.admin;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.customer.CustomerAddRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("AdminCustomerController")
@RequestMapping("admin/customer")
@Api(tags = "B端-客户管理接口")
@Slf4j
public class CustomerController {
    @Resource
    private CustomerService customerService;

    // region 增删改查
    /**
     * 创建
     *
     * @param customerAddRequest
     * @return
     */
    @Operation(summary = "增加客户")
    @PostMapping("/add")
    public BaseResponse<String> addCustomer(@RequestBody CustomerAddRequest customerAddRequest) {
        return customerService.addCustomerByAdmin(customerAddRequest);
    }
    /**
     * 更新客户
     *
     * @param customerUpdateRequest
     * @return
     */
    @Operation(summary = "更新客户")
    @PostMapping("/update")
    public BaseResponse<String> updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        return customerService.updateCustomer(customerUpdateRequest);
    }
}
