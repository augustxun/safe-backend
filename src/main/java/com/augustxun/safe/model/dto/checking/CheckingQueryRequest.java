package com.augustxun.safe.model.dto.checking;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CheckingQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal serviceFee;
    private BigDecimal balance;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
