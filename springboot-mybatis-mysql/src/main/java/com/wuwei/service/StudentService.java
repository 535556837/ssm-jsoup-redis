package com.wuwei.service;

import com.wuwei.entity.Result;
import com.wuwei.entity.Student;
import java.util.List;

/**
 * Service接口
 *
 * @author 吴维
 * @date 2017-8-6 20:31:29
 */
public interface StudentService {

    public Result addStudent(Student student);

    public Result getAllStudent();

    public Result getStudentById(Long id);

    public Result updateStudent(Student student);

    public Result delStudentById(Long id);
}
