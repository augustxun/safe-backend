package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.model.dto.student.StudentQueryRequest;
import com.augustxun.safe.model.entity.Student;
import com.augustxun.safe.service.StudentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-StudentLoan账户管理接口")
@RequestMapping("admin/student")
public class StudentController {
    @Resource
    private StudentService studentService;
    /**
     * Student账户信息分页查询
     *
     * @param studentQueryRequest
     * @return
     */
    @Operation(summary = "Student账户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Student>> listStudentByPage(@RequestBody StudentQueryRequest studentQueryRequest) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        Page<Student> studentPage = studentService.page(new Page<>(current, size), studentService.getQueryWrapper(studentQueryRequest));
        return ResultUtils.success(studentPage);
    }
}
