package com.augustxun.safe.model.dto.savings;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SavingsQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long acctNo;
    private BigDecimal interestRate;
    private BigDecimal balance;
}
