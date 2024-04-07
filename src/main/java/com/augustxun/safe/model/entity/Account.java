package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName account
 */
@Data
@TableName(value ="account")
public class Account implements Serializable {
    /**
     * 
     */
    private Integer acctno;

    /**
     * 
     */
    private String acctname;

    /**
     * 
     */
    private String azipcode;

    /**
     * 
     */
    private String aunit;

    /**
     * 
     */
    private String astreet;

    /**
     * 
     */
    private String acity;

    /**
     * 
     */
    private String astate;

    /**
     * 
     */
    private Date adateopened;

    /**
     * 
     */
    private String atype;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}