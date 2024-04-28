package com.augustxun.safe.controller.admin;

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

@RestController("AdminCustomerController")
@RequestMapping("admin/customer")
@Api(tags = "B端-客户管理接口")
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
    @Operation(summary = "增加客户")
    @PostMapping("/add")
    public BaseResponse<String> addCustomer(@RequestBody CustomerAddRequest customerAddRequest, HttpServletRequest request) {
        if (customerAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerAddRequest, customer);
        // 校验
        customerService.validCustomer(customer, true);
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.OPERATION_ERROR, "请先登录");
        // 1.当前为管理员操作
        if (loginUser.getUserRole().equals("admin")) {
            boolean result = customerService.save(customer);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            return ResultUtils.success("用户信息添加成功");
        }
        // 2.当前为普通用户在操作
        Long userId = loginUser.getId();
        Customer oldCustomer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("userId", userId));
        if (oldCustomer != null) { //2.1 数据表中已经有客户信息
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "已存在，不可重复创建");
        }
        // 2.2 数据表中尚没有客户信息
        customer.setUserId(userId);
        boolean result = customerService.save(customer);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Long newCustomerId = customerMapper.selectOne(new QueryWrapper<Customer>().eq("userId", userId)).getId();
        loginUser.setCustomerId(newCustomerId);
        userService.updateById(loginUser);
        return ResultUtils.success("用户信息添加成功");
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @Operation(summary = "删除客户")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteCustomer(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        long id = Long.parseLong(deleteRequest.getId());

        if (deleteRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 仅管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = customerService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param customerUpdateRequest
     * @return
     */
    @Operation(summary = "更新客户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest) {
        long id = Long.parseLong(customerUpdateRequest.getId());
        if (customerUpdateRequest == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerUpdateRequest, customer);
        customer.setId(id);
        // 参数校验
        customerService.validCustomer(customer, false);

        // 判断是否存在
        Customer oldCustomer = customerService.getById(id);
        ThrowUtils.throwIf(oldCustomer == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = customerService.updateById(customer);
        return ResultUtils.success(result);
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
