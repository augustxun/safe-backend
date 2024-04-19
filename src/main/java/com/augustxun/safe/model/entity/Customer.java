package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName customer
 */
@TableName(value ="customer")
@Data
public class Customer implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;


    private String lastName;

    private String firstName;

    private String zipcode;

    private String unit;

    private String street;

    private String city;

    private String state;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}