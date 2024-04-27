package com.augustxun.safe.model.dto.customer;

import java.io.Serializable;
import java.util.List;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

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