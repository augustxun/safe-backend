package com.augustxun.safe.model.dto.loan;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanQueryRequest extends PageRequest {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long acctNo;
    private BigDecimal rate;
    private BigDecimal amount;
    private Integer months;
    private BigDecimal payment;
    private String loanType;
}
