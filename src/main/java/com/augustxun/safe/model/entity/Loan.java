package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName loan
 */
@TableName(value ="loan")
@Data
public class Loan implements Serializable {
    private Long acctNo;

    private BigDecimal rate;

    private BigDecimal amount;

    private Integer months;

    private BigDecimal payment;

    private String loanType;

    private Long customerId;

    private static final long serialVersionUID = 1L;
}