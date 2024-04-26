package com.augustxun.safe.service.impl;

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

}




