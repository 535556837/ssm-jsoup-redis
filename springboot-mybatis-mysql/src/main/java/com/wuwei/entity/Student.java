package com.wuwei.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Student implements Serializable {

    private static final long serialVersionUID = 4740717975029751603L;
    
    private Long id;
    private String name;
    private String gender;
    private String course;
    private Timestamp addTime;

    public Student() {}

    public Student(Long id, String name, String gender, String course, Timestamp addTime) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.course = course;
        this.addTime = addTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", gender=" + gender + ", course=" + course + ", addTime=" + addTime + '}';
    }

}
