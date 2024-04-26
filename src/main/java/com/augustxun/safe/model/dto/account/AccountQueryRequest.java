package com.augustxun.safe.model.dto.account;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountQueryRequest extends PageRequest implements Serializable {

    private String acctName;

    private String city;

    private String state;

    private String type;

    private String userId;
}