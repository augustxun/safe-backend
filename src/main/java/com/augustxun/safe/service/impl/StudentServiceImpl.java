package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.model.dto.student.StudentQueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.augustxun.safe.model.entity.Student;
import com.augustxun.safe.service.StudentService;
import com.augustxun.safe.mapper.StudentMapper;
import org.springframework.stereotype.Service;

/**
* @author augustxun
* @description 针对表【student】的数据库操作Service实现
* @createDate 2024-04-26 00:04:25
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    @Override
    public BaseResponse<String> addStudentAccount(Long acctNo) {
        Student student = new Student();
        student.setAcctNo(acctNo);
        student.setStuId("zl5203");
        student.setUniversityName("XXX University");
        student.setGradYear(1900);
        student.setGradMonth(12);
        this.save(student);
        return ResultUtils.success("创建成功");
    }

    @Override
    public QueryWrapper<Student> getQueryWrapper(StudentQueryRequest studentQueryRequest) {
        return null;
    }
}




