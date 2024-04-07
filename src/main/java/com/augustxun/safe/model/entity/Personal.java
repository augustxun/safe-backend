package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName personal
 */
@TableName(value ="personal")
@Data
public class Personal implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer acctno;

    /**
     * 
     */
    private BigDecimal income;

    /**
     * 
     */
    private Integer creditscore;

    /**
     * 
     */
    private String purpose;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}