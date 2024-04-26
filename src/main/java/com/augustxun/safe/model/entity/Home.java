package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName home
 */
@TableName(value ="home")
@Data
public class Home implements Serializable {
    private Long acctNo;

    private Integer builtYear;

    private Integer insureAcctNo;

    private BigDecimal yearlyPremium;

    private Long insureComId;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}