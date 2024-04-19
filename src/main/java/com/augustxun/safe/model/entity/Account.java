package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName account
 */
@TableName(value ="account")
@Data
public class Account implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer acctNo;

    private String acctName;

    private String zipcode;

    private String unit;

    private String street;

    private String city;

    private String state;

    private Date dateOpened;

    private String type;

    private Long userId;

    private static final long serialVersionUID = 1L;
}