package com.wuwei.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class PiData implements Serializable {

    
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String gender;
    private String course;
    private Timestamp addTime;

    public PiData() {}

    public PiData(Long id, String name, String gender, String course, Timestamp addTime) {
        this.name = name;
        this.gender = gender;
        this.course = course;
        this.addTime = addTime;
    }

  

}
