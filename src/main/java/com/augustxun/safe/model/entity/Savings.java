package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName savings
 */
@TableName(value = "savings")
@Data
public class Savings implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long acctNo;
    private BigDecimal interestRate;
    private BigDecimal balance;
    private Long customerId;
}