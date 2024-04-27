package com.augustxun.safe.model.dto.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 */
@Data
@ApiModel
public class AccountAddRequest implements Serializable {
    private String acctName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;
    private String type;
    private String loanType;
    private String userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}