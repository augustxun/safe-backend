package com.augustxun.safe.model.dto.customer;

import com.augustxun.safe.common.PageRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *

 */
@Data
@ApiModel
public class CustomerQueryRequest extends PageRequest implements Serializable {

    private String lastName;
    private String firstName;
    private String street;
    private String city;
    private String state;
}