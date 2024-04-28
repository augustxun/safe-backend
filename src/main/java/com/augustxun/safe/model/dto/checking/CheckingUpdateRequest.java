package com.augustxun.safe.model.dto.checking;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CheckingUpdateRequest {
    private String acctNo;
    private BigDecimal serviceFee;
    private BigDecimal balance;
}
