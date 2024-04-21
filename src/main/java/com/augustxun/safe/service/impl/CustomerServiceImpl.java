package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.mapper.CustomerMapper;
import com.augustxun.safe.model.dto.customer.CustomerQueryRequest;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(firstName) || StringUtils.isAnyBlank(lastName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
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
}




