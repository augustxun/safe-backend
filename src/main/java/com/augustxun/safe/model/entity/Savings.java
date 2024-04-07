package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName savings
 */
@TableName(value ="savings")
@Data
public class Savings implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer acctno;

    /**
     * 
     */
    private BigDecimal interestrate;

    /**
     * 
     */
    private Integer cid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}