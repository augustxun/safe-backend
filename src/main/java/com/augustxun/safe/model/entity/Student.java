package com.augustxun.safe.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName student
 */
@TableName(value ="student")
@Data
public class Student implements Serializable {
    private Long acctNo;

    private String universityName;

    private String stuId;

    private Integer gradMonth;

    private Integer gradYear;

    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}