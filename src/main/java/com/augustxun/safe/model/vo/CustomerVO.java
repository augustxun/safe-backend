package com.augustxun.safe.model.vo;

import com.augustxun.safe.model.entity.Customer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CustomerVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String lastName;
    private String firstName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;

    public static CustomerVO objToVo(Customer customer) {
        if (customer == null) return null;
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customer, customerVO);
        return customerVO;
    }
}
