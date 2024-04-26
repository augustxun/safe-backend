package com.augustxun.safe.model.dto.account;

import com.augustxun.safe.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccountQueryRequest extends PageRequest implements Serializable {
    private String acctName;
    private String city;
    private String state;
    private String type;
    private String userId;
}