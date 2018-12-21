package com.wuwei.dao;

import com.wuwei.entity.Student;
import java.util.List;

/**
 *
 * @author 吴维
 * @date 2017-8-5 21:03:48
 */
public interface Dao {

    public int addStudent(Student student);
    
    public List<Student> getAllStudent();

    public Student getStudentById(Long id);

    public int updateStudent(Student student);

    public int delStudentById(Long id);
    
}
