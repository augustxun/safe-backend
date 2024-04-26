package com.augustxun.safe.model.dto.student;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class StudentQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long acctNo;
    private String universityName;
    private String stuId;
    private Integer gradMonth;
    private Integer gradYear;
    private Long isDelete;
}
