package com.augustxun.safe.model.dto.loan;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class LoanUpdateRequest {
    private String acctNo;
    private BigDecimal rate;
    private BigDecimal amount;
    private Integer months;
    private BigDecimal payment;
    private String loanType;
}
