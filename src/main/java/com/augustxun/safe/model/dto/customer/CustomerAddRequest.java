package com.augustxun.safe.model.dto.customer;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
@ApiModel
public class CustomerAddRequest implements Serializable {

    private String lastName;

    private String firstName;

    private String zipcode;

    private String unit;

    private String street;

    private String city;

    private String state;

    private String userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}