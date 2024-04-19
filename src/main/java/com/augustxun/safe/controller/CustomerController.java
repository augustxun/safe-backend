package com.augustxun.safe.controller;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.model.dto.customer.CustomerAddRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/customer")
@Api(tags = "CustomerController")
@Slf4j
public class CustomerController {
    @Resource
    private CustomerService customerService;

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
    @PostMapping("/add")
    public BaseResponse<Long> addCustomer(@RequestBody CustomerAddRequest customerAddRequest, HttpServletRequest request) {
        if (customerAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerAddRequest, customer);
        // 校验
        customerService.validCustomer(customer, true);
        User loginUser = userService.getLoginUser(request);
        customer.setUserId(loginUser.getId());
        boolean result = customerService.save(customer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newCustomerId = customer.getId();
        return ResultUtils.success(newCustomerId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCustomer(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Customer oldCustomer = customerService.getById(id);
        ThrowUtils.throwIf(oldCustomer == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldCustomer.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = customerService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param customerUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        if (customerUpdateRequest == null || customerUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerUpdateRequest, customer);
        // 参数校验
        customerService.validCustomer(customer, false);
        long id = customerUpdateRequest.getId();
        // 判断是否存在
        Customer oldCustomer = customerService.getById(id);
        ThrowUtils.throwIf(oldCustomer == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = customerService.updateById(customer);
        return ResultUtils.success(result);
    }
}
