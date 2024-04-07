package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName student
 */
@TableName(value ="student")
@Data
public class Student implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer acctno;

    /**
     * 
     */
    private String universityname;

    /**
     * 
     */
    private String stuid;

    /**
     * 
     */
    private Integer gradmonth;

    /**
     * 
     */
    private Integer gradyear;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}