package com.wuwei.service;

import com.wuwei.dao.Dao;
import com.wuwei.entity.Result;
import com.wuwei.entity.Student;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 服务层Service
 *
 * @author 吴维
 * @date 2017-8-5 14:49:21
 */
@Service
public class StudentServiceImpl implements StudentService {
    
    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());

    @Resource
    private Dao dao;

    @Override
    public Result addStudent(Student student) {
        Result result = new Result();
        try {
            dao.addStudent(student);
            result.setStatus(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result getAllStudent() {
        Result result = new Result();
        try {
            List<Student> students = dao.getAllStudent();
            result.setStatus(1);
            result.setData(students);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result getStudentById(Long id) {
        Result result = new Result();
        try {
            Student student = dao.getStudentById(id);
            result.setStatus(1);
            result.setData(student);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result updateStudent(Student student) {
        Result result = new Result();
        try {
            dao.updateStudent(student);
            result.setStatus(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public Result delStudentById(Long id) {
        Result result = new Result();
        try {
            dao.delStudentById(id);
            result.setStatus(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }
}
