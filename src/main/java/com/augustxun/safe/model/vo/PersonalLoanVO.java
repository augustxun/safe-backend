package com.augustxun.safe.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PersonalLoanVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long acctNo;
    private String acctName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;
    private Date dateOpened;
    private String type;
    private Long userId;
    // Loan 属性
    private BigDecimal rate;
    private BigDecimal amount;
    private Integer months;
    private BigDecimal payment;
    private String loanType;
    // Personal Loan属性
    private BigDecimal income;
    private BigDecimal creditScore;
    private String purpose;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
