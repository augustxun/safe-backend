package com.augustxun.safe.model.dto.personal;

import com.augustxun.safe.common.PageRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel
public class PersonalQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long acctNo;
    private BigDecimal income;
    private BigDecimal creditScore;
    private String purpose;
    private Integer isDelete;
}
