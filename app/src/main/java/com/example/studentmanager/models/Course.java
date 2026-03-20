package com.example.studentmanager.models;

import java.io.Serializable;

/**
 * 课程实体类
 */
public class Course implements Serializable {
    private String id;          // 课程号
    private String name;        // 课程名称
    private int credit;         // 学分

    public Course() {}

    public Course(String id, String name, int credit) {
        this.id = id;
        this.name = name;
        this.credit = credit;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }
}
