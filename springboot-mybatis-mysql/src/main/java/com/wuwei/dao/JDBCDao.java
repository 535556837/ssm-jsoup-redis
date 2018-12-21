package com.wuwei.dao;

import com.wuwei.entity.Student;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 数据访问层Dao
 *
 * @author 吴维
 * @date 2017-8-5 21:16:05
 */
//@Repository("JDBCDao")
public class JDBCDao implements Dao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class StudentRowMapper implements RowMapper<Student> {

        @Override
        public Student mapRow(ResultSet rs, int i) throws SQLException {
            Student student = new Student();
            student.setId(rs.getLong("id"));
            student.setName(rs.getString("name"));
            student.setGender(rs.getString("gender"));
            student.setCourse(rs.getString("course"));
            student.setAddTime(rs.getTimestamp("addTime"));
            return student;
        }
    };

    @Override
    public int addStudent(Student student) {
        String sql = "insert into student(name,gender,course)values(?,?,?)";
        String name = student.getName();
        String gender = student.getGender();
        String course = student.getCourse();
        return jdbcTemplate.update(sql, new Object[]{name, gender, course});
    }

    @Override
    public List<Student> getAllStudent() {
        String sql = "select * from student";
        return jdbcTemplate.query(sql, new StudentRowMapper());
    }

    @Override
    public Student getStudentById(Long id) {
        String sql = "select * from student where id = ?";
        List<Student> students = jdbcTemplate.query(sql, new Object[]{id}, new StudentRowMapper());
        if (students != null && students.size() > 0) {
            return students.get(0);
        }
        return null;
    }

    @Override
    public int updateStudent(Student student) {
        String sql = "update student set name = ?, gender = ?, course = ? where id = ?";
        Long id = student.getId();
        String name = student.getName();
        String gender = student.getGender();
        String course = student.getCourse();
        return jdbcTemplate.update(sql, new Object[]{name, gender, course, id});
    }

    @Override
    public int delStudentById(Long id) {
        String sql = "delete from student where id = ?";
        return jdbcTemplate.update(sql, new Object[]{id});
    }
}
