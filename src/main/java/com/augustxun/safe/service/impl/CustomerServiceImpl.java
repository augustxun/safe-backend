package com.augustxun.safe.service.impl;

import cn.hutool.core.util.StrUtil;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
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
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author augustxun
 * @description 针对表【customer】的数据库操作Service实现
 * @createDate 2024-04-18 21:42:59
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
        implements CustomerService {
    @Resource
    private UserService userService;

    public void validCustomer(Customer customer, boolean add) {
        if (customer == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        Long userId = customer.getId();
        // 创建时，一些特定的参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(firstName) || StringUtils.isAnyBlank(lastName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (userId == null || userId <= 0) {throw new BusinessException(ErrorCode.PARAMS_ERROR);}
        }
    }


    @Transactional
    @Override
    public BaseResponse<String> updateCustomer(CustomerUpdateRequest customerUpdateRequest) {
        if (customerUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.根据 id 查询数据库customer是否存在
        Long id = Long.parseLong(customerUpdateRequest.getId());
        if (id == null) {
            return ResultUtils.success("id 信息异常");
        }
        Customer oldCustomer = this.getById(id);
        // 1.1 不存在则抛出异常
        ThrowUtils.throwIf(oldCustomer == null, ErrorCode.NOT_FOUND_ERROR);
        // 1.2 存在，用 customer 对象去更新
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerUpdateRequest, customer);
        customer.setId(id);
        // 2.更新数据库，返回结果
        boolean result = this.updateById(customer);
        return ResultUtils.success("更新成功！");
    }

    @Override
    @Transactional

    public BaseResponse<String> addCustomerByAdmin(CustomerAddRequest customerAddRequest) {
        if (customerAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取请求体的 userId
        String uId = customerAddRequest.getUserId();
        if (StrUtil.isBlank(uId)) { // 检查 userId 是否为空
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "管理员操作时，userId 参数不可为空");
        }
        Long userId = Long.parseLong(uId);
        // 查看是否有 userId 对应的用户
        User user = userService.getById(userId);
        userService.updateById(user);
        if (user == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "创建失败，该User不存在");
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerAddRequest, customer);
        customer.setUserId(userId);
        this.save(customer);
        Customer newCustomer = this.query().eq("userId", userId).one();

        user.setCustomerId(newCustomer.getId());
        userService.updateById(user);
        return ResultUtils.success("用户信息添加成功");
    }

    @Override
    @Transactional
    public BaseResponse<String> addCustomerByUser(CustomerAddRequest customerAddRequest, HttpServletRequest request) {
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
        Customer oldCustomer = query().eq("userId", userId).one();
        // 3.1 数据表中已经有客户信息
        if (oldCustomer != null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "已存在，不可重复创建");
        }

        // 3.2 用 customer 对象保存客户信息, 将customer 对象的userId 属性设置为当前用户的 userId
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerAddRequest, customer);
        customer.setUserId(userId);
        this.save(customer);
        // 3.3 将当前用户的 customerId 也设置为customer表中数据的id
        Customer newCustomer = query().eq("userId", userId).one();
        Long newCustomerId = newCustomer.getId();
        loginUser.setCustomerId(newCustomerId);
        userService.updateById(loginUser);
        // 4.返回结果
        return ResultUtils.success("用户信息添加成功");
    }
}




