package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName home
 */
@TableName(value = "home")
@Data
public class Home implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long acctNo;
    private Integer builtYear;
    private Long insureAcctNo;
    private BigDecimal yearlyPremium;
    private Long insureComId;
    private Integer isDelete;
}