package com.augustxun.safe.model.dto.personal;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PersonalUpdateRequest {
    private String acctNo;
    private BigDecimal income;
    private BigDecimal creditScore;
    private String purpose;
}
