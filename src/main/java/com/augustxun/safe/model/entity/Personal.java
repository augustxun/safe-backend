package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName personal
 */
@TableName(value = "personal")
@Data
public class Personal implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long acctNo;
    private BigDecimal income;
    private BigDecimal creditScore;
    private String purpose;
    private Integer isDelete;
}