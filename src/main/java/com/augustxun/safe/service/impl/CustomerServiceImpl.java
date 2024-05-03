package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.CustomerMapper;
import com.augustxun.safe.model.dto.customer.CustomerQueryRequest;
import com.augustxun.safe.model.dto.customer.CustomerUpdateRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author augustxun
 * @description 针对表【customer】的数据库操作Service实现
 * @createDate 2024-04-18 21:42:59
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
        implements CustomerService {
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
            if (userId == null || userId <= 0) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public QueryWrapper<Customer> getQueryWrapper(CustomerQueryRequest customerQueryRequest) {
        if (customerQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String firstName = customerQueryRequest.getFirstName();
        String lastName = customerQueryRequest.getLastName();
        String city = customerQueryRequest.getCity();
        String state = customerQueryRequest.getState();
        String sortField = customerQueryRequest.getSortField();
        String sortOrder = customerQueryRequest.getSortOrder();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(firstName), "firstName", firstName);
        queryWrapper.like(StringUtils.isNotBlank(lastName), "lastName", lastName);
        queryWrapper.like(StringUtils.isNotBlank(city), "city", city);
        queryWrapper.like(StringUtils.isNotBlank(state), "state", state);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

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
        // 2.更新数据库，返回结果
        boolean result = this.updateById(customer);
        return ResultUtils.success("更新成功！");
    }
}




