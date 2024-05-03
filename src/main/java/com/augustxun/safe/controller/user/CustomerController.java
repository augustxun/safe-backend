package com.augustxun.safe.controller.user;


import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.CustomerMapper;
import com.augustxun.safe.model.dto.customer.CustomerAddRequest;
import com.augustxun.safe.model.dto.customer.CustomerQueryRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

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
        // 1.检查请求体
        if (customerAddRequest == null) { // 请求体为 null 时直接返回
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.获取当前登录用户信息
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "请先登录！");
        }
        // 3.检查该用户是否已经创建客户信息
        Long userId = loginUser.getId();
        Customer oldCustomer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("userId", userId));
        // 3.1 数据表中已经有客户信息
        if (oldCustomer != null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "已存在，不可重复创建");
        }

        // 3.2 用 customer 对象保存客户信息, 将customer 对象的userId 属性设置为当前用户的 userId
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerAddRequest, customer);
        customer.setUserId(userId);
        customerService.save(customer);
        // 3.3 将当前用户的 customerId 也设置为customer表中数据的id
        Long newCustomerId = customerMapper.selectOne(new QueryWrapper<Customer>().eq("userId", userId)).getId();
        loginUser.setCustomerId(newCustomerId);
        userService.updateById(loginUser);
        // 4.返回结果
        return ResultUtils.success("用户信息添加成功");
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
