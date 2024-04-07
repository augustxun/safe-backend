package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName loan
 */
@TableName(value ="loan")
@Data
public class Loan implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer acctno;

    /**
     * 
     */
    private BigDecimal lrate;

    /**
     * 
     */
    private BigDecimal lamount;

    /**
     * 
     */
    private Integer lmonths;

    /**
     * 
     */
    private BigDecimal lpayment;

    /**
     * 
     */
    private String ltype;

    /**
     * 
     */
    private Integer cid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}