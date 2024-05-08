package com.augustxun.safe.model.dto.customer;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *

 */
@Data
@ApiModel
public class CustomerUpdateRequest implements Serializable {
    private String id;
    private String lastName;
    private String firstName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;
}