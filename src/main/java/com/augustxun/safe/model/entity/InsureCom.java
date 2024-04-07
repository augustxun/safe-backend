package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName insure_com
 */
@TableName(value ="insure_com")
@Data
public class InsureCom implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer iid;

    /**
     * 
     */
    private String iname;

    /**
     * 
     */
    private String izipcode;

    /**
     * 
     */
    private String iunit;

    /**
     * 
     */
    private String istreet;

    /**
     * 
     */
    private String icity;

    /**
     * 
     */
    private String istate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}