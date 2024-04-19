package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.model.entity.Customer;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.mapper.CustomerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author augustxun
* @description 针对表【customer】的数据库操作Service实现
* @createDate 2024-04-18 21:42:59
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService{
    public void validCustomer(Customer customer, boolean add) {
        if (customer == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        // 创建时，参数不能为空
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(firstName) || StringUtils.isAnyBlank(lastName) ) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(firstName) && firstName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }
}




