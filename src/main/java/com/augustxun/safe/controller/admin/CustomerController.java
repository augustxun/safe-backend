package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.model.dto.customer.CustomerAddRequest;
import com.augustxun.safe.model.dto.customer.CustomerQueryRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.service.CustomerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 更新
     *
     * @param customerUpdateRequest
     * @return
     */
    @Operation(summary = "更新客户")
    @PostMapping("/update")
    public BaseResponse<String> updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        return customerService.updateCustomer(customerUpdateRequest);
    }


    /**
     * 分页获取列表（仅管理员）
     *
     * @param customerQueryRequest
     * @return
     */
    @Operation(summary = "客户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Customer>> listCustomerByPage(@RequestBody CustomerQueryRequest customerQueryRequest) {
        int current = customerQueryRequest.getCurrent();
        int size = customerQueryRequest.getPageSize();
        Page<Customer> customerPage = customerService.page(new Page<>(current, size), customerService.getQueryWrapper(customerQueryRequest));
        return ResultUtils.success(customerPage);
    }

}
