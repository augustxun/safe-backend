package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName home
 */
@TableName(value ="home")
@Data
public class Home implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer acctno;

    /**
     * 
     */
    private Integer builtyear;

    /**
     * 
     */
    private Integer insureacctno;

    /**
     * 
     */
    private BigDecimal yearlypremium;

    /**
     * 
     */
    private Integer iid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}