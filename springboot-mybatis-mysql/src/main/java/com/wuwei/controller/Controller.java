package com.wuwei.controller;

import com.wuwei.entity.Result;
import com.wuwei.entity.Student;
import com.wuwei.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器Controller 
 * 请求地址：http://ip:port/contextPath/servletPath
 * 例如：http://localhost:8181/demo/student/getAllStudent
 *
 * @author 吴维
 * @date 2017-8-5 14:52:54
 */
//@CrossOrigin
@RestController
@RequestMapping("/student")
public class Controller {

    @Autowired
    private StudentService studentService;

    //增加(Create)
    @RequestMapping("/addStudent")
    public Result addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    //查询(Retrieve)
    @RequestMapping("/getAllStudent")
    public Result getAllStudent() {
        return studentService.getAllStudent();
    }

    //根据ID查询
    @RequestMapping("/getStudentById")
    public Result getStudentById(@RequestParam("id") Long id) {
        return studentService.getStudentById(id);
    }

    //更新(Update)
    @RequestMapping("/updateStudent")
    public Result updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    //删除(Delete)
    @RequestMapping("/delStudentById")
    public Result delStudentById(@RequestParam("id") Long id) {
        return studentService.delStudentById(id);
    }
}
