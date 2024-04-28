package com.augustxun.safe.model.dto.account;

import com.augustxun.safe.common.PageRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class AccountQueryRequest extends PageRequest implements Serializable {
    private String acctNo;
    private String acctName;
    private String city;
    private String state;
    private String type;
}