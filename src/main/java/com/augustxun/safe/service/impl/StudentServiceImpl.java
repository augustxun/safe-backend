package com.augustxun.safe.service.impl;

import com.augustxun.safe.mapper.StudentMapper;
import com.augustxun.safe.model.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author augustxun
 * @description 针对表【student】的数据库操作Service实现
 * @createDate 2024-04-05 17:23:11
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
        implements IService<Student> {

}




