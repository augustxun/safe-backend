package com.augustxun.safe.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class HomeLoanVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long acctNo;
    private String acctName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;
    private Date dateOpened;
    private String type;
    private String loanType;
    // Loan 属性
    private BigDecimal rate;
    private BigDecimal amount;
    private Integer months;
    private BigDecimal payment;
    // Home Loan属性
    private Integer builtYear;
    private Long insureAcctNo;
    private BigDecimal yearlyPremium;
    private Long insureComId;
    //
    private String userName;
}
