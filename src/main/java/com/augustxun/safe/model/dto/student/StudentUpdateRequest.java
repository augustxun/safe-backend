package com.augustxun.safe.model.dto.student;

import lombok.Data;

@Data
public class StudentUpdateRequest {
    private String acctNo;
    private String universityName;
    private String stuId;
    private Integer gradMonth;
    private Integer gradYear;
}
