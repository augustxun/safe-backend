package com.augustxun.safe.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SavingsVO {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long acctNo;
    private String acctName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOpened;
    private String type;
    // Saving 独有属性
    private BigDecimal balance;
    private BigDecimal interestRate;
    private String userName;
}
