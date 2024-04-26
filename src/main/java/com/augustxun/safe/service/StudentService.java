package com.augustxun.safe.service;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.model.dto.home.HomeQueryRequest;
import com.augustxun.safe.model.dto.student.StudentQueryRequest;
import com.augustxun.safe.model.entity.Home;
import com.augustxun.safe.model.entity.Student;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author augustxun
* @description 针对表【student】的数据库操作Service
* @createDate 2024-04-26 00:04:25
*/
public interface StudentService extends IService<Student> {
    /**
     * 新建一个 Student 贷款账户
     * @param acctNo
     * @return
     */
    public BaseResponse<String> addStudentAccount(Long acctNo);

    /**
     * Student账户查询条件
     *
     * @param studentQueryRequest
     * @return
     */
    public QueryWrapper<Student> getQueryWrapper(StudentQueryRequest studentQueryRequest);
}
