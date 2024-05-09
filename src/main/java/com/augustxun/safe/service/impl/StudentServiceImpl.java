package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.StudentMapper;
import com.augustxun.safe.model.dto.student.StudentQueryRequest;
import com.augustxun.safe.model.dto.student.StudentUpdateRequest;
import com.augustxun.safe.model.entity.Student;
import com.augustxun.safe.service.StudentService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author augustxun
 * @description 针对表【student】的数据库操作Service实现
 * @createDate 2024-04-26 00:04:25
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Transactional
    @Override
    public BaseResponse<String> addStudentLoanAccount(Long acctNo) {
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
        if (studentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String sortField = studentQueryRequest.getSortField();
        String sortOrder = studentQueryRequest.getSortOrder();
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Transactional
    @Override
    public BaseResponse<Boolean> updateStudent(StudentUpdateRequest studentUpdateRequest) {
        long acctNo = Long.parseLong(studentUpdateRequest.getAcctNo());

        if (studentUpdateRequest == null || acctNo <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Student student = new Student();
        BeanUtils.copyProperties(studentUpdateRequest, student);
        student.setAcctNo(acctNo);
        // 判断是否存在
        Student oldStudent = this.getById(acctNo);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = this.updateById(student);
        return ResultUtils.success(result);
    }
}




