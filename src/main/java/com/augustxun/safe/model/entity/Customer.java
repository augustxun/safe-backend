package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}