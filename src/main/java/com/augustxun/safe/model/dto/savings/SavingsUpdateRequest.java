package com.augustxun.safe.model.dto.savings;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SavingsUpdateRequest {
    private String acctNo;
    private BigDecimal interestRate;
    private BigDecimal balance;
}
