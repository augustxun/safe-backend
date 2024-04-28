package com.augustxun.safe.model.dto.home;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomeUpdateRequest {
    private String acctNo;
    private Integer builtYear;
    private String insureAcctNo;
    private BigDecimal yearlyPremium;
    private String insureComId;
}
