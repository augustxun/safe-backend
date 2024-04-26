package com.augustxun.safe.model.dto.home;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomeQueryRequest extends PageRequest {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long acctNo;
    private Integer builtYear;
    private Long insureAcctNo;
    private BigDecimal yearlyPremium;
    private Long insureComId;
    private Integer isDelete;
}
