package com.augustxun.safe.model.vo;

import com.augustxun.safe.model.entity.Account;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentLoanVO implements Serializable {
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
    // Student Loan属性
    private String universityName;
    private String stuId;
    private Integer gradMonth;
    private Integer gradYear;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
