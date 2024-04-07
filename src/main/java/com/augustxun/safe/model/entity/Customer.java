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
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer cid;

    /**
     * 
     */
    private String clastname;

    /**
     * 
     */
    private String cfirstname;

    /**
     * 
     */
    private String czipcode;

    /**
     * 
     */
    private String cunit;

    /**
     * 
     */
    private String cstreet;

    /**
     * 
     */
    private String ccity;

    /**
     * 
     */
    private String cstate;

    private String phone;
    private String password;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}