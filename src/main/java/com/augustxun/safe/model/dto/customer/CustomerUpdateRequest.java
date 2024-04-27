package com.augustxun.safe.model.dto.customer;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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